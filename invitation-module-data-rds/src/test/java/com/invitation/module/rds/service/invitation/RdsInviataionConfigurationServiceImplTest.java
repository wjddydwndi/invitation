package com.invitation.module.rds.service.invitation;

import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.model.user.User;
import com.invitation.module.rds.config.TestConfiguration;
import com.invitation.module.rds.repository.privacy.configuration.PrivacyConfigurationRepository;
import com.invitation.module.rds.repository.privacy.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest(classes = TestConfiguration.class)
class RdsInviataionConfigurationServiceImplTest {

    @Autowired private UserRepository userRepository;
    @Autowired private PrivacyConfigurationRepository privacyConfigurationRepository;

    @Test
    void findAll() {
        List<User> list = userRepository.findAll();

        Assertions.assertNotNull(list);
    }

    @Test
    void findMaxUpdateTime() {

        Configuration configuration = privacyConfigurationRepository.findMaxUpdateTime();
    }

}
