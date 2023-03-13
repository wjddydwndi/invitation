package com.invitation.module.api.service.config;

import com.invitation.module.common.model.configuration.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = TestConfiguration.class)
@ActiveProfiles("")
class CacheInvitationDatabaseSyncTest {

    @Autowired CacheInvitationDatabaseSync cacheInvitationDatabaseSync;

    @Test
    void test1() {
        System.out.println("dddd");
    }
    @Test
    void setConfigurations() {

        String key = "invitation-t_configuration";
        List<Configuration> list = new ArrayList<>();
        Configuration configuration = new Configuration();
        configuration.setCategory("CONFIG");
        configuration.setCode("MODE");
        configuration.setValue("DEV");
        configuration.setDescription("SERVER MODE");
        configuration.setEnabled(true);
        configuration.setReference(false);
        list.add(configuration);

        Configuration configuration1 = new Configuration();
        configuration1.setCategory("CONFIG");
        configuration1.setCode("ALLOWED_PAGE");
        configuration1.setValue("Y");
        configuration1.setDescription("진입 허용 페이지 ");
        configuration1.setEnabled(true);
        configuration1.setReference(true);
        list.add(configuration1);

        Configuration configuration2 = new Configuration();
        configuration2.setCategory("CONFIG");
        configuration2.setCode("SUPER_USER");
        configuration2.setValue("hj.jeong@sbicosmoney.com");
        configuration2.setDescription("마스터");
        configuration2.setEnabled(true);
        configuration2.setReference(false);
        list.add(configuration2);

        cacheInvitationDatabaseSync.setConfigurations(key, list);
    }



}