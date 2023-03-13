package com.invitation.module.api.service.token;

import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;

public interface AuthService {

    Token generateToken(User user);
    boolean isTokenValidate(User user, Token token, Token compare);
    Token renewToken(User user, Token token);
    boolean isExpiration(String userId, String token);
    boolean tokenProcess(String userId, String accessToken, String refreshToken);
}
