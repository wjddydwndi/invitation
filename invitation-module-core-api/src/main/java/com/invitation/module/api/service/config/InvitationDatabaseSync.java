package com.invitation.module.api.service.config;

import com.invitation.module.common.logger.DetailLogger;
import static com.invitation.module.common.model.configuration.ConfigValues.*;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.rds.service.invitation.configuration.RdsInvitationConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class InvitationDatabaseSync implements DatabaseSync {

    private Map<String, List<Configuration>> gConfigurations = new ConcurrentHashMap<>();
    private final RdsInvitationConfigurationService invitationConfigurationService;

    @Override
    public Map<String, Date> getLastUpdate() {
        return createCompareData(invitationConfigurationService.getConfigUpdateTime());
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

    @Override
    public String getDatabaseName() {
        return CONFIG_DATABASE_INVITATION.DATABASE();
    }

    @Override
    public void replace(String key, Configuration asIs, Configuration toBe) {

        if (CommonsUtil.isEmpty(asIs) || CommonsUtil.isEmpty(toBe)) {
            DetailLogger.error("There are no settings synchronization parameters. asIs={}, toBe={}", asIs, toBe);
            return;
        }

        List<Configuration> configurations = gConfigurations.get(key);
        configurations.remove(asIs);
        configurations.add(toBe);

    }


}
