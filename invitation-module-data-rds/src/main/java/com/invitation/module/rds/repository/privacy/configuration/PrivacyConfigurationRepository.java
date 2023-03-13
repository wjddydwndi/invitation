package com.invitation.module.rds.repository.privacy.configuration;

import com.invitation.module.common.model.configuration.Configuration;

import java.util.Date;
import java.util.List;

public interface PrivacyConfigurationRepository {

    Configuration findMaxUpdateTime();
    List<Configuration> findAll();
    List<Configuration> findLastUpdateTime(Date lastTime);
}
