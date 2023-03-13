package com.invitation.module.api.service.config;

import com.invitation.module.common.model.configuration.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;


@SpringBootTest(classes = TestConfiguration.class)
@ActiveProfiles("")
class ConfigServiceImplTest {

    @Autowired
    private ConfigService configService;

    @Autowired
    @Qualifier("invitationDatabaseSync")
    private DatabaseSync databaseSync1;

    @Autowired
    @Qualifier("cacheInvitationDatabaseSync")
    private DatabaseSync cacheInvitationDatabaseSync;


    @Autowired
    @Qualifier("privacyDatabaseSync")
    private DatabaseSync databaseSync2;


    @Test
    @DisplayName("loadConfiguration")
    void loadConfiguration() {

        configService.loadConfiguration();

        List<Configuration> list = cacheInvitationDatabaseSync.getConfiguration("t_configuration", "ALLOWED_PAGE");

        Assertions.assertNotNull(list);

    }

    @Test
    @DisplayName("syncConfiguration")
    void syncConfiguration() {
    }

    @Test
    @DisplayName("setConfiguration")
    void setConfiguration() {
    }

    @Test
    @DisplayName("getConfiguration")
    void getConfiguration() {
    }
}