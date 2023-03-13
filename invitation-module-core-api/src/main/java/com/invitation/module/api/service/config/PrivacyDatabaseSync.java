package com.invitation.module.api.service.config;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.rds.service.invitation.configuration.RdsInvitationConfigurationService;
import com.invitation.module.rds.service.privacy.configuration.RdsPrivacyConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class PrivacyDatabaseSync implements DatabaseSync {
    private Map<String, List<Configuration>> gConfigurations = new ConcurrentHashMap<>();
    private final RdsPrivacyConfigurationService rdsPrivacyConfigurationService;

    @Override
    public Map<String, Date> getLastUpdate() {
        return createCompareData(rdsPrivacyConfigurationService.getConfigUpdateTime());
    }

    public Map<String, Date> createCompareData(List<Configuration> list) {
        Map<String, Date> map = new ConcurrentHashMap<>();

        if (!CommonsUtil.isEmpty(list)) {
            for (Configuration configuration : list) {

                String key = CommonsUtil.createConfigurationKey(configuration);
                Date updateDate = configuration.getUpdateTime();

                map.put(key, updateDate);
            }
        }

        return map;
    }

    @Override
    public void setConfigurations(String key, List<Configuration> configurations) {
        if (CommonsUtil.isEmpty(key) || CommonsUtil.isEmpty(configurations)) {
            DetailLogger.error("There are no settings synchronization parameters. configurations={}", configurations);
            return;
        }

        gConfigurations.put(key, configurations);
        DetailLogger.debug("Update settings gConfigurations={}", configurations);
    }

    @Override
    public Map<String, List<Configuration>> getConfigurations() {
        return gConfigurations;
    }

    @Override
    public List<Configuration> getConfiguration(String key, String code) {

        if (CommonsUtil.isEmpty(key) || CommonsUtil.isEmpty(code)) {
            return null;
        }

        List<Configuration> configurations = gConfigurations.get(key);
        List<Configuration> configurationList = (List<Configuration>) configurations.stream().filter(x-> x.getCode().equalsIgnoreCase(code));

        if (!configurationList.isEmpty()) {
            return configurationList;
        }

        return null;
    }

    public String getDatabaseName() {
        return ConfigValues.CONFIG_DATABASE_PRIVACY.DATABASE();
    }

    @Override
    public void replace(String key, Configuration asIs, Configuration toBe) {
        return;
    }

}
