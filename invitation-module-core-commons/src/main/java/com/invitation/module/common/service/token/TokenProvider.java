package com.invitation.module.common.service.token;

import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;

public interface TokenProvider {
    /**
     * JWT Process
     * 1. Client 로그인 시도 (ID/PW)
     * 2. Server는 Client 요청 확인 후, JWT 생성 후, 전달
     * 3. Client Header에 JWT 실어서 Server에 요청
     * 4. Server는 JWT Signature를 확인 후, Client에게 데이터 반환
     *
     * 1. client 요청시 server는 access-token, refresh-token 발급
     * 2. client는 매 요청시, access-token 을 헤더에 담아 요청
     * 3. server는 access-token 만료시 만료되었다는 response를 한다.
     * 4. client는 만료시 refresh-token을 보냄.
     * 5. server는 refresh-token 의 유효성 검사 진행 - 새로운 access-token 발급
     * refresh-token 발급시, DB에 저장.
     * **/
    // 토큰 생성
    String generateToken(User user, long expireDuration);
    String createToken(Claims claims, SecretKey secretKey, long expireDuration);
    // 토큰 유효성 검사
    boolean isVerifiyToken(String userId, String token);
    // 토큰 만료기한 가져오기
    Date getTokenExpirationDate(String userId, String token);
    // 토큰 만료여부 확인
    boolean isTokenExpired(String userId, String token);
    // Claim 디코딩
    Claims getClaims(String userId, String token);
    // Claim에서 특정 key 값 가져오기
    String getTokenInfoByKey(String userId, String token, String key);

    Long getExpireDuration(long tokenDuration);

    SecretKey getSecretKey(User user);
    SecretKey getSecretKey(String userId);

}
