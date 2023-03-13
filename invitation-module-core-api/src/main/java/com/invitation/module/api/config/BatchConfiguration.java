package com.invitation.module.api.config;

import com.invitation.module.api.batch.ConfigurationUpdater;
import com.invitation.module.api.service.config.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final ConfigService configService;

    @Bean
    public ConfigurationUpdater configurationUpdater() {
        return new ConfigurationUpdater(configService);
    }
}
