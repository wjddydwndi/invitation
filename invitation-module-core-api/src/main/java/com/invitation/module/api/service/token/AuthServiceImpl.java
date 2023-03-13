package com.invitation.module.api.service.token;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;
import com.invitation.module.common.service.token.TokenService;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.common.util.exception.TokenInvalidException;
import com.invitation.module.common.util.exception.RedisSaveException;
import com.invitation.module.rds.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TokenService tokenService;

    private final RedisService redisTokenServiceImpl;

    private final RedisService redisUserServiceImpl;

    @Override
    public Token generateToken(User user) {
        if (CommonsUtil.isEmpty(user)) {
            DetailLogger.error("토큰 생성을 위한 파라미터가 부족합니다.");
            throw new NullPointerException("parameter is null");
        }

        Token token = null;
        try {
            token = tokenService.generateToken(user);
            if (redisTokenServiceImpl.save(token) < 1) {
                DetailLogger.error("토큰 생성 레디스 등록 실패");
                throw new RedisSaveException("레디스 토큰 등록 중 예외 발생");
            }
        } catch (Exception e) {
            DetailLogger.error("레디스 등록중 예외 발생 e={}", e.getMessage());
            throw e;
        }

        return token;
    }

    @Override
    public boolean isTokenValidate(User user, Token token, Token compare) {

        if (CommonsUtil.isEmpty(user) || CommonsUtil.isEmpty(token) || CommonsUtil.isEmpty(compare)) {
            DetailLogger.error("토큰 유효성 체크를 위한 파라미터가 부족합니다. user={}, token={}, compare={}", user, token, compare);
            return false;
        }
        // jwt check
        if (tokenService.isValidateTokenByJwt(user, token) == false) {
            return false;
        }

        // redis-token 과 비교
        if (tokenService.isValidateTokenByParam(user, token, compare) == false) {
            return false;
        }

        return false;
    }

    @Override
    public Token renewToken(User user, Token token) {

        if (CommonsUtil.isEmpty(user) || CommonsUtil.isEmpty(token)) {
            DetailLogger.error("토큰 갱신을 위한 파라미터가 부족합니다. user={}, token={}", user, token);
            throw new NullPointerException("parameter is null");
        }

        Token compare = null;
        try {
            compare = (Token) redisTokenServiceImpl.findById(user.getId());
        } catch (Exception e) {
            DetailLogger.error("Redis 데이터 조회중 예외 발생 e={}", e.getMessage());
            return token;
        }

        if (isTokenValidate(user, token, compare) == false) {
            DetailLogger.error("유효하지 않은 토큰입니다.. user={}, token={}, compare={}", user, token, compare);
            throw new TokenInvalidException("token is not validate");
        }

        Token renewToken = tokenService.renewToken(token, user);
        DetailLogger.info("토큰이 갱신되었습니다. token.userId={}", renewToken.getUserId());

        return renewToken;
    }

    @Override
    public boolean isExpiration(String userId, String token) {

        if (CommonsUtil.isEmpty(userId) || CommonsUtil.isEmpty(token)) {
            DetailLogger.error("토큰 만료 확인을 위한 파라미터가 부족합니다. user={}, token={}", userId, token);
            throw new NullPointerException("parameter is null");
        }


        if (tokenService.isExpiration(userId, token) == true) return false;

        Token t = (Token) redisTokenServiceImpl.findById(userId);

        if (CommonsUtil.isEmpty(t)) {
            throw new NullPointerException("Key does not exist.");
        }

        if (t.getAccessTokenExpireTime().before(new Date())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean tokenProcess(String userId, String accessToken, String refreshToken) {

        if (CommonsUtil.isEmpty(userId) || CommonsUtil.isEmpty(accessToken) || CommonsUtil.isEmpty(refreshToken)) {
            DetailLogger.error("토큰 만료 확인을 위한 파라미터가 부족합니다. user={}, accessToken={}, refreshToken={}", userId, accessToken, refreshToken);
            return false;
        }

        Token token = (Token) redisTokenServiceImpl.findById(userId);

        if (CommonsUtil.isEmpty(token)) {
            DetailLogger.error("token does not exist in redis");
            return false;
        }

        if (!token.getAccessToken().equals(accessToken)) {
            DetailLogger.error("accessToken is not matching");
            return false;
        }

        if (!token.getRefreshToken().equals(refreshToken)) {
            DetailLogger.error("refreshToken is not matching");
            return false;
        }

        if (isExpiration(userId, accessToken) == false) {// 만료

            User user = (User) redisUserServiceImpl.findById(userId);

            if (CommonsUtil.isEmpty(user)) {
                DetailLogger.error("user data is Null");
                return false;
            }

            Token renewToken = renewToken(user, token);
            DetailLogger.info("토큰이 만료되어 갱신합니다. userId={}, renew-access-token", userId, renewToken.getAccessToken());
            return true;
        }

        return true;
    }

    // security 적용시
    /*public Authentication authenticationProcess(String userId, String accessToken, String refreshToken) {

        if (CommonsUtil.isEmpty(userId) || CommonsUtil.isEmpty(accessToken) || CommonsUtil.isEmpty(refreshToken)) {
            DetailLogger.error("토큰 인증 값을 가져오기 위한 파라미터가 부족합니다. user={}, accessToken={}", userId, accessToken, refreshToken);
            throw new NullPointerException("parameter is Null");
        }

        if (CommonsUtil.isEmpty(accessToken) || CommonsUtil.isEmpty(refreshToken)) {
            DetailLogger.error("토큰 인증 값을 가져오기 위한 파라미터가 부족합니다. accessToken={}, refreshToken={}", accessToken, refreshToken);
            throw new NullPointerException("parameter is Null");
        }

        Token token = (Token) redisTokenServiceImpl.findById(userId);

        if (CommonsUtil.isEmpty(token) || !token.getAccessToken().equals(accessToken) || !token.getRefreshToken().equals(refreshToken)) {
            DetailLogger.error("유효하지 않은 토큰 userId={}, accessToken={}, refreshToken={}", userId, accessToken, refreshToken);
            throw new TokenInvalidException("유효하지 않은 토큰");
        }

        User user = (User) redisUserServiceImpl.findById(userId);

        if (CommonsUtil.isEmpty(user)) {
            DetailLogger.error("user data is Null");
            throw new NullPointerException("parameter is Null");
        }

        if (isExpiration(userId, token.getAccessToken()) == false) {// 만료됐을 경우 갱신

            Token renewToken = null;
            try {

                renewToken = renewToken(user, token);
                if (redisTokenServiceImpl.save(renewToken) < 1) {
                    throw new RedisSaveException("레디스 토큰 갱신 중 예외 발생");
                }

            } catch (Exception e) {
                DetailLogger.error("레디스 토큰 갱신 중 예외 발생 e={}", e.getMessage());
                throw e;
            }
            return new UsernamePasswordAuthenticationToken(renewToken, null, user.getAuthorities());
        }

        return new UsernamePasswordAuthenticationToken(token, null, user.getAuthorities());
    }*/
}
