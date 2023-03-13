package com.invitation.module.rds.service.privacy.user;

import com.invitation.module.common.model.user.User;

import java.util.List;

public interface RdsUserService {

    List<User> findAll();
    User findById(String userId);
    User findByEmail(String email);
    long updateLoginTry(String userId);

}
