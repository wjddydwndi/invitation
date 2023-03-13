package com.invitation.module.common.service.token;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.common.util.CryptoUtil;
import com.invitation.module.common.util.exception.TokenExpirationException;
import com.invitation.module.common.util.exception.TokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value(value = "${token.access-expiration}")
    private long accessTokenDuration;

    @Value(value = "${token.refresh-expiration}")
    private long refreshTokenDuration;

    private final TokenProvider tokenProvider;

    private final CryptoUtil cryptoUtil;

    @Override
    public Token generateToken(User user) {

        String accessToken = tokenProvider.generateToken(user, accessTokenDuration);    // access-token 생성
        String refreshToken = tokenProvider.generateToken(user, refreshTokenDuration);  // refresh-token 생성

        // db 및 redis 에 Token 등록하기.
        String encryptAccessToken = encryptAesToken(accessToken);
        String encryptRefreshToken = encryptAesToken(refreshToken);

        if (CommonsUtil.isEmpty(encryptAccessToken) || CommonsUtil.isEmpty(encryptRefreshToken)) {
            DetailLogger.error("암호화된 토큰 값이 없습니다. encryptAccessToken={}, encryptRefreshToken={}", encryptAccessToken, encryptRefreshToken);
            throw new NullPointerException("encrypted token is null");
        }

        return makeToken(user, encryptAccessToken, encryptRefreshToken);
    }

     @Override
    public boolean isValidateToken(String userId, String token) {

        return tokenProvider.isVerifiyToken(userId, token);
    }

    public boolean isValidateTokenByJwt(User user, Token token) {

        if (CommonsUtil.isEmpty(user) || CommonsUtil.isEmpty(token)) {
            DetailLogger.error("토큰 유효성 체크를 위한 파라미터가 부족합니다.. token={}, user={}", token, user);
            return false;
        }

        String decryptedAccessToken = decryptAesToken(token.getAccessToken());
        String decryptedRefreshToken = decryptAesToken(token.getRefreshToken());

        String userId = user.getId();
        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();

        if (isValidateToken(userId, accessToken) == false) {
            DetailLogger.error("유효한 토큰이 아닙니다. token={}, user={}", token, user);
            return false;
        }

        if (isValidateToken(userId, refreshToken) == false) {
            DetailLogger.error("유효한 토큰이 아닙니다. token={}, user={}", token, user);
            return false;
        }

        return true;
    }

    @Override
    public boolean isValidateTokenByParam(User user, Token token, Token compare) {

        if (CommonsUtil.isEmpty(user) || CommonsUtil.isEmpty(token) || CommonsUtil.isEmpty(compare)) {
            DetailLogger.error("토큰 유효성 체크를 위한 파라미터가 부족합니다.. token={}, user={}, compare={}", token, user, compare);
            return false;
        }

        String decryptedAccessToken = decryptAesToken(token.getAccessToken());
        String decryptedRefreshToken = decryptAesToken(token.getRefreshToken());

        String decryptedCompareAccessToken = decryptAesToken(compare.getAccessToken());
        String decryptedCompareRefreshToken = decryptAesToken(compare.getRefreshToken());

        if (!decryptedAccessToken.equals(decryptedCompareAccessToken)) {
            DetailLogger.error("유효한 토큰이 아닙니다. access token={}, compare={}", decryptedAccessToken, decryptedCompareAccessToken);
            return false;
        }

        if (!decryptedRefreshToken.equals(decryptedCompareRefreshToken)) {
            DetailLogger.error("유효한 토큰이 아닙니다. refresh token={}, compare={}", decryptedRefreshToken, decryptedCompareRefreshToken);
            return false;
        }

        return true;
    }

    @Override
    public boolean isExpiration(String userId, String token) {

        if (CommonsUtil.isEmpty(userId) || CommonsUtil.isEmpty(token)) {
            DetailLogger.error("유효한 토큰이 아닙니다. userId={}, token={}", userId, token);
            throw new NullPointerException("parameter is null");
        }

        return tokenProvider.isTokenExpired(userId, token);
    }

    @Override
    public Token renewToken(Token token, User user) {

        if (CommonsUtil.isEmpty(token) || CommonsUtil.isEmpty(user)) {
            DetailLogger.error("토큰을 갱신하기 위한 파라미터가 부족합니다. token={}, user={}", token, user);
            return token;
        }

        String userId = user.getId();

        String renewAccessToken = tokenProvider.generateToken(user, accessTokenDuration);       // 새로운 access-token 생성
        String renewRefreshToken = tokenProvider.generateToken(user, refreshTokenDuration);     // 새로운 refresh-token 생성

        String encryptAccessToken = encryptAesToken(renewAccessToken);
        String encryptRefreshToken = encryptAesToken(renewRefreshToken);

        Date accessExpire = tokenProvider.getTokenExpirationDate(userId, renewAccessToken);
        Date refreshExpire = tokenProvider.getTokenExpirationDate(userId, renewRefreshToken);

        return makeToken(token, encryptAccessToken, encryptRefreshToken, accessExpire, refreshExpire);
    }

    @Override
    public String decryptAesToken(String token) {

        String decryptedToken = null;
        try {

            decryptedToken = cryptoUtil.decryptAES256(token);

        } catch (Exception e) {
            DetailLogger.error("토큰 갱신 중 예외 발생 : 토큰 decrypt 예외 발생 e={}", e.getMessage());
            return token;
        }

        if (CommonsUtil.isEmpty(decryptedToken)) {
            DetailLogger.error("토큰 갱신중 예외 발생 token={}, decryptedAccessToken={}", token, decryptedToken);
            return token;
        }

        return decryptedToken;
    }

    public String encryptAesToken(String token) {

        String encryptedToken = null;
        try {

            encryptedToken = cryptoUtil.encryptAES256(token);

        } catch (Exception e) {
            DetailLogger.error("토큰 갱신 중 예외 발생 : 토큰 decrypt 예외 발생 e={}", e.getMessage());
            return token;
        }

        if (CommonsUtil.isEmpty(encryptedToken)) {
            DetailLogger.error("토큰 갱신중 예외 발생 token={}, decryptedAccessToken={}", token, encryptedToken);
            return token;
        }

        return encryptedToken;
    }


    @Override
    public Token makeToken(User user, String accessToken, String refreshToken) {

        if (CommonsUtil.isEmpty(user) || CommonsUtil.isEmpty(accessToken) || CommonsUtil.isEmpty(refreshToken)) {
            DetailLogger.error("토큰을 생성하기 위한 데이터가 부족합니다. user={}, accessToken={}, refreshToken={}", user, accessToken, refreshToken);
            return null;
        }

        Date now = new Date();
        long accessTokenExpireTime = now.getTime() + accessTokenDuration;
        long refreshTokenExpireTime = now.getTime() + refreshTokenDuration;

        Token token = new Token();
        token.setUserId(user.getId());
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setAccessTokenExpireTime(new Date(accessTokenExpireTime));
        token.setRefreshTokenExpireTime(new Date(refreshTokenExpireTime));
        token.setCreateTime(now);

        return token;
    }

    public Token makeToken(Token token, String accessToken, String refreshToken, Date accessTokenExpire, Date RefreshTokenExpire) {

        if (CommonsUtil.isEmpty(token)) {
            DetailLogger.error("토큰을 재생성하기 위한 데이터가 부족합니다. token={}", token);
            return null;
        }

        if (CommonsUtil.isEmpty(accessToken) && CommonsUtil.isEmpty(refreshToken)) {
            DetailLogger.error("토큰을 재생성하기 위한 데이터가 부족합니다. accessToken={}, refreshToken={}", accessToken, refreshToken);
            return null;
        }

        Date now = new Date();

        int renewCnt = token.getRenewCnt();

        Token renewToken = new Token();
        renewToken.setUserId(token.getUserId());
        renewToken.setAccessToken(accessToken);
        renewToken.setRefreshToken(refreshToken);
        renewToken.setAccessTokenExpireTime(accessTokenExpire);
        renewToken.setRefreshTokenExpireTime(RefreshTokenExpire);
        renewToken.setRenewCnt(++renewCnt);
        renewToken.setUpdateTime(now);
        renewToken.setCreateTime(token.getCreateTime());

        return renewToken;
    }
}
