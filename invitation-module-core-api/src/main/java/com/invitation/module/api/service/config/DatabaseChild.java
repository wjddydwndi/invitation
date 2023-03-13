package com.invitation.module.api.service.config;

import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.model.configuration.Configuration;

import java.util.List;

public interface DatabaseChild {

    DatabaseSync importReference(String key, DatabaseSync databaseSync);
    void importReference(ConfigValues configValue, DatabaseSync databaseSync);
}
