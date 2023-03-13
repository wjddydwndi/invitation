package com.invitation.module.rds.repository.invitation.configuration;

import com.invitation.module.common.model.configuration.Configuration;

import java.util.Date;
import java.util.List;

public interface InvitationConfigurationRepository {
    List<Configuration> findMaxUpdateTime();
    List<Configuration> findAll();
    List<Configuration> findLastUpdateTime(Date lastTime);
}
