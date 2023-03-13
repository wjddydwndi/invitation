package com.invitation.module.rds.repository.privacy.user;

import com.invitation.module.common.model.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
    List<User> findAll();
    User findById(String userId);
    User findByEmail(String email);

}
