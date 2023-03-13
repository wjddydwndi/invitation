package com.invitation.module.api.batch;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.api.service.config.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class ConfigurationUpdater {

    private final ConfigService configService;

    @Scheduled(fixedDelay = 10 * 1000)
    public void ConfigurationUpdateProc() {

        try {
            configService.loadConfiguration();

        } catch (Exception e) {
            e.getStackTrace();
            DetailLogger.error("Exception while synchronizing system preferences e={}", e.getMessage());
        }
    }
}