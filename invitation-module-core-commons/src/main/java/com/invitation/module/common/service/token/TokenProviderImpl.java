package com.invitation.module.common.service.token;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.common.util.CryptoUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {

    @Value("${token.secret}")
    private String secret;

    @Override
    public String generateToken(User user, long expireDuration) {

        if (CommonsUtil.isEmpty(expireDuration) || CommonsUtil.isEmpty(user)) {
            DetailLogger.info("토큰 생성 파라미터가 충분하지 않습니다. User={}, expireDuration={}", user, expireDuration);
            return null;
        }

        Claims claims = Jwts.claims();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());

        return createToken(claims, getSecretKey(user), expireDuration);
    }

    @Override
    public String createToken(Claims claims, SecretKey secretKey, long expireDuration) {

        if (CommonsUtil.isEmpty(claims) || CommonsUtil.isEmpty(secretKey) || CommonsUtil.isEmpty(expireDuration)) {
            DetailLogger.error("토큰 생성 파라미터가 충분하지 않습니다. claims={}, secretKey={}, expireDuration={}", claims, secretKey, expireDuration);
            return null;
        }

        Date now = new Date();
        long expiration = now.getTime() + expireDuration;

        String accessToken = "";

        try {
            accessToken = Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setSubject("access-token")
                    .setClaims(claims)
                    .setIssuer("invitation") // 토큰 발급자
                    .setIssuedAt(now) // 토큰 발급 시간
                    .setExpiration(new Date(expiration)) // 토큰 만료시간
                    .signWith(secretKey, SignatureAlgorithm.HS256) // 알고리즘 시크릿 키
                    .compact();

        } catch (Exception e) {
            e.getStackTrace();
            DetailLogger.error("토큰 생성 중 예외 발생, e={}", e.getMessage());
        }

        return accessToken;
    }


    @Override
    /**
     *  0 : false
     *  1 : true
     *  2 : 유효기간 만료
     * **/
    public boolean isVerifiyToken(String userId, String token) {

        if (CommonsUtil.isEmpty(userId) || CommonsUtil.isEmpty(token)) {
            DetailLogger.error("토큰 인증을 위한 파라미터가 충분하지 않습니다. userId={}, token={}", userId, token);
            return false;
        }

        Claims claims = null;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey(userId))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            DetailLogger.debug("토큰 유효성 검사 claims={}", claims);

        } catch (ExpiredJwtException expiredJwtException) { // 토큰이 만료했을 경우
            DetailLogger.error("토큰이 만료되었습니다. userId={}, token={}", userId, token);
            return false;
        } catch (JwtException jwtException){
            jwtException.getStackTrace();
            DetailLogger.error("토큰 유효성 검사 중 에러 발생, e={}", jwtException.getMessage());
            return false;
        } catch(Exception e) {
            e.getStackTrace();
            DetailLogger.error("토큰 검사 중 에러 발생, e={}",e.getMessage());
            return false;
        }

        return true;
    }



    @Override
    public Date getTokenExpirationDate(String userId, String token) {
        if (CommonsUtil.isEmpty(userId) || CommonsUtil.isEmpty(token)) {
            DetailLogger.error("토큰 만료를 조회하기 위한 파라미터가 부족합니다. userId={}, token={}", userId, token);
            return new Date();
        }

        Claims claims = getClaims(userId, token);
        return claims.getExpiration();
    }

    @Override
    public boolean isTokenExpired(String userId, String token) {
        if (CommonsUtil.isEmpty(userId) || CommonsUtil.isEmpty(token)) {
            DetailLogger.error("토큰을 조회하기 위한 파라미터가 부족합니다. userId={}, token={}", userId, token);
            return true;
        }

        return getTokenExpirationDate(userId, token).before(new Date());
    }


    @Override
    public Claims getClaims(String userId, String token) {
        if (CommonsUtil.isEmpty(userId) || CommonsUtil.isEmpty(token)) {
            DetailLogger.error("토큰을 조회하기 위한 파라미터가 부족합니다. token={}", token);
            return null;
        }

        return Jwts.parserBuilder().setSigningKey(getSecretKey(userId)).build().parseClaimsJws(token).getBody();
    }

    @Override
    public String getTokenInfoByKey(String userId, String token, String key) {
        if (CommonsUtil.isEmpty(userId)|| CommonsUtil.isEmpty(token) || CommonsUtil.isEmpty(key)) {
            DetailLogger.error("토큰을 조회하기 위한 파라미터가 부족합니다. token={}, key={}", token, key);
            return null;
        }
        return String.valueOf(getClaims(userId, token).get(key));
    }

    @Override
    public Long getExpireDuration(long tokenDuration) {
        return 1000L * tokenDuration;// 토큰 만료
    }

    @Override
    public SecretKey getSecretKey(User user) {
        if (CommonsUtil.isEmpty(user)) {
            DetailLogger.error("secret key를 생성하기 위한 파라미터가 부족합니다. user={}", user);
        }

        String key = user.getId().concat(secret);

        SecretKey secretKey = null;

        try {
            String secretKeyEncodeBase64 = Encoders.BASE64.encode(key.getBytes());
            byte[] keyBytes = Decoders.BASE64.decode(secretKeyEncodeBase64);
            secretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            e.getStackTrace();
            DetailLogger.error("secretKey 생성중 에러 발생 e={}", e.getMessage());
            return null;
        }

        return secretKey;
    }

    @Override
    public SecretKey getSecretKey(String userId) {
        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("secret key를 생성하기 위한 파라미터가 부족합니다. user={}", userId);
        }

        String key = userId.concat(secret);

        SecretKey secretKey = null;

        try {
            String secretKeyEncodeBase64 = Encoders.BASE64.encode(key.getBytes());
            byte[] keyBytes = Decoders.BASE64.decode(secretKeyEncodeBase64);
            secretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            e.getStackTrace();
            DetailLogger.error("secretKey 생성중 에러 발생 e={}", e.getMessage());
            return null;
        }

        return secretKey;
    }
}
