package com.invitation.module.api.service.user;

import com.invitation.module.common.model.rest.ServerResponse;
import com.invitation.module.common.model.user.User;

public interface UserService {

    User getUserInfo(String userId);
    User getUserInfo(String email, String encEmail);
    long updateLoginTry(String userId);
    ServerResponse login(User user);
    boolean comparePassword(String infoPassword, String password, String encPassword);

}
