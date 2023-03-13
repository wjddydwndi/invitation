package com.invitation.module.rds.service.privacy.user;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.user.User;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.common.util.exception.UnsufficientParamException;
import com.invitation.module.rds.repository.privacy.user.LoginTryRepository;
import com.invitation.module.rds.repository.privacy.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RdsUserServiceImpl implements RdsUserService {

    private final UserRepository userRepository;
    private final LoginTryRepository loginTryRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String userId) {

        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("Insufficient parameters. userId={}", userId);
            return null;
        }

        return userRepository.findById(userId);
    }

    public User findByEmail(String email) {

        if (CommonsUtil.isEmpty(email)) {
            DetailLogger.error("Insufficient parameters. userId={}", email);
            return null;
        }

        return userRepository.findById(email);
    }

    public long updateLoginTry(String userId) {

        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("Insufficient parameters. userId={}", userId);
            throw new UnsufficientParamException("Insufficient parameters.");
        }

        return loginTryRepository.updateLoginTry(userId);
    }
}
