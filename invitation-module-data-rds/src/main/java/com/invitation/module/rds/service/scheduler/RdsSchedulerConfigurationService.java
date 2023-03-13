package com.invitation.module.rds.service.scheduler;

import com.invitation.module.common.model.configuration.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface RdsSchedulerConfigurationService {

    <T> T importConfigurationByDatabaseV2(String repository, String method);

}
