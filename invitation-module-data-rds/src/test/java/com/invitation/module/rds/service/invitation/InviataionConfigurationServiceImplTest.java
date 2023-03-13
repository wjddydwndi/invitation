package com.invitation.module.rds.service.invitation;

import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.rds.config.TestConfiguration;
import com.invitation.module.rds.service.invitation.configuration.RdsInviataionConfigurationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest(classes = TestConfiguration.class)
@ActiveProfiles("")
class InviataionConfigurationServiceImplTest {

    @PersistenceContext(unitName = "invitationEntityManager")
    private EntityManager entityManager;

    @Autowired RdsInviataionConfigurationServiceImpl inviataionConfigurationService;
    @Test
    void getConfigUpdateTime() {

        List<Configuration> configurations = inviataionConfigurationService.getConfigUpdateTime();

    }
}