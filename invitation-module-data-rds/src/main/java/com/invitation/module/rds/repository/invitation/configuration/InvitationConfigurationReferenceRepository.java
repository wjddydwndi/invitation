package com.invitation.module.rds.repository.invitation.configuration;

import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.model.configuration.ConfigurationReference;

import java.util.Date;
import java.util.List;

public interface InvitationConfigurationReferenceRepository {
    ConfigurationReference findMaxUpdateTime();
    List<ConfigurationReference> findAll();
    List<ConfigurationReference> findLastUpdateTime(Date lastTime);
}
