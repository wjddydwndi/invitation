package com.invitation.module.api.service.config;

import com.invitation.module.rds.config.database.DatabaseConfigInvitation;
import com.invitation.module.rds.config.database.DatabaseConfigPrivacy;
import com.invitation.module.rds.config.database.QuerydslConfiguration;
import com.invitation.module.rds.config.redis.RedisConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Configuration
@Import({
        DatabaseConfigInvitation.class,
        DatabaseConfigPrivacy.class,
        RedisConfiguration.class,
        QuerydslConfiguration.class
})

@ComponentScan(
        useDefaultFilters = false,
        basePackages = {"com.invitation.module.api", "com.invitation.module.rds", "com.invitation.module.common" },
        includeFilters = {
                @ComponentScan.Filter(value = Configuration.class, type = FilterType.ANNOTATION),
                @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION),
                @ComponentScan.Filter(value = Service.class, type = FilterType.ANNOTATION),
                @ComponentScan.Filter(value = Repository.class, type = FilterType.ANNOTATION),
                @ComponentScan.Filter(value = Component.class, type = FilterType.ANNOTATION),
        }
)
public class TestConfiguration {
        public TestConfiguration() {}
}
