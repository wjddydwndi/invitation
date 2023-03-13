package com.invitation.module.rds.service.scheduler;

import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.model.configuration.ConfigurationReference;
import com.invitation.module.rds.config.TestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TestConfiguration.class)
class RdsSchedulerConfigurationServiceImplTest {

    @Autowired
    RdsSchedulerConfigurationService schedulerConfigurationService;

    @Test
    void test1() {

        String database = "invitation";
        String table = "t_configuration_reference";
        List<ConfigurationReference> list = schedulerConfigurationService.importConfigurationByDatabaseV2(database, table);

        Assertions.assertNotNull(list);
    }
}