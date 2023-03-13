package com.invitation.module.common.service.token;

import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;

import java.util.Date;

public interface TokenService {
/**
 * JWT
 * - Header (서명 알고리즘, 토큰 타입)
 * - Payload ( 의미있는 정보 key-value)
 * - Signature (서명 = Header+Payload+secret)
 *
 * Claim 은 payload 부분에 해당함.
 **/
    Token generateToken(User user); // 토큰 생성
    boolean isValidateToken(String userId, String token);
    boolean isValidateTokenByJwt(User user, Token token); // 토큰 유효성 검사
    boolean isValidateTokenByParam(User user, Token token, Token compare);
    boolean isExpiration(String userId, String token);
    Token makeToken(User user, String accessToken, String refreshToken);
    Token makeToken(Token token, String accessToken, String refreshToken, Date accessTokenExpire, Date RefreshTokenExpire);
    Token renewToken(Token token, User user);
    String encryptAesToken(String token);
    String decryptAesToken(String token);
}
