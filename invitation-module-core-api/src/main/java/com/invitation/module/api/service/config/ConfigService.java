package com.invitation.module.api.service.config;

import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.model.configuration.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ConfigService {

    void loadConfiguration();
    void syncConfiguration(DatabaseSync databaseSync, Map<String, List<Configuration>> configurations);
    <T> T importDatabaseByTable(String database, String table);
    List<Configuration> getConfiguration(String database, String table, String category, String code);
    List<Configuration> loadConfigurations(ConfigValues configValue);
}
