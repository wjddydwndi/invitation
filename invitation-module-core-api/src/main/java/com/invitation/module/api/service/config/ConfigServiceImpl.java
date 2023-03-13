package com.invitation.module.api.service.config;

import com.invitation.module.api.service.util.ApiUtils;
import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.model.configuration.ConfigurationReference;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.rds.service.redis.RedisService;
import com.invitation.module.rds.service.scheduler.RdsSchedulerConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import static com.invitation.module.common.model.configuration.ConfigValues.*;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService, DatabaseChild {

    private final DatabaseSync invitationDatabaseSync;

    private final DatabaseSync privacyDatabaseSync;

    private final DatabaseSync cacheInvitationDatabaseSync;

    private final RdsSchedulerConfigurationService schedulerConfigurationService;

    private final RedisService redisConfigurationServiceImpl;

    @Override
    public void loadConfiguration() {
        DetailLogger.info("Load Configuration...");

        try {
            syncConfiguration(invitationDatabaseSync, invitationDatabaseSync.getConfigurations());
            syncConfiguration(privacyDatabaseSync, privacyDatabaseSync.getConfigurations());

        } catch (Exception ex) {
            ex.getStackTrace();
            DetailLogger.error("Error synchronizing system settings, e={}", ex.getMessage());
        }

    }

    public void syncConfiguration(DatabaseSync databaseSync, Map<String, List<Configuration>> configurations) {

        if (CommonsUtil.isEmpty(databaseSync)) {
            DetailLogger.error("databaseSync instance is Null, databaseSync={}", databaseSync);
            return;
        }

        Map<String, Date> lastUpdateMap = databaseSync.getLastUpdate();



        for (String key : lastUpdateMap.keySet()) {

            DatabaseSync imported = null;

            if (configurations.containsKey(key)) {

                Date prevUpdate = new Date();
                List<Configuration> list = configurations.get(key);

                Optional<Configuration> optionalConfig = list.stream().sorted(Comparator.comparing(Configuration::getUpdateTime).reversed()).findFirst();
                if (optionalConfig.isPresent()) {
                    prevUpdate = optionalConfig.get().getUpdateTime();
                }

                Date lastUpdate = lastUpdateMap.get(key);

                if (prevUpdate.compareTo(lastUpdate) < 0) {
                    // 테이블단위 동기화화
                    databaseSync.setConfigurations(key, importDatabaseByTable(databaseSync.getDatabaseName(), key));
                    imported = importReference(key, databaseSync);
                }
            } else {
                String table = key.substring(key.lastIndexOf("-") + 1);
                List<Configuration> list = importDatabaseByTable(databaseSync.getDatabaseName(), table);
                databaseSync.setConfigurations(key, list);
                imported = importReference(key, databaseSync);
            }

            // redis update
            if (!CommonsUtil.isEmpty(imported)) {
                Map<String, List<Configuration>> configurationMap = imported.getConfigurations();
                redisConfigurationServiceImpl.save(key, configurationMap.get(key));
            }
        }
    }

    @Override
    public List<Configuration> getConfiguration(String database, String table, String category, String code) {

        if (CommonsUtil.isEmpty(database) || CommonsUtil.isEmpty(table) || CommonsUtil.isEmpty(category) || CommonsUtil.isEmpty(code)) {
            DetailLogger.error("Insufficient parameters to get settings. database={}, table={}, category={}, code={}");
            return null;
        }

        DetailLogger.info("database={}, table={}, category={}, code={}", database, table, category, code);

        if (CONFIG_DATABASE_INVITATION.DATABASE().equals(database.toUpperCase())) {
            if (ApiUtils.isProd() == false) {
                return cacheInvitationDatabaseSync.getConfiguration(table, code);
            } else {
                return invitationDatabaseSync.getConfiguration(table, code);
            }
        }
        else if (CONFIG_DATABASE_PRIVACY.DATABASE().equals(database.toUpperCase())) {
            return privacyDatabaseSync.getConfiguration(table, code);
        }

        return null;
    }

    @Override
    public <T> T importDatabaseByTable(String database, String table) {

        if (CommonsUtil.isEmpty(database) || CommonsUtil.isEmpty(table)) {
            DetailLogger.error("Latest Data Import Exception Occurred database={}, table={}", database, table);
            return null;
        }

        return (T) schedulerConfigurationService.importConfigurationByDatabaseV2(database, table);
    }


    public DatabaseSync importReference(String key, DatabaseSync databaseSync) {

        if (CommonsUtil.isEmpty(key) ) {
            DetailLogger.error("configValue is Empty, key={}", key);
            return databaseSync;
        }

        Map<String, List<Configuration>> configurationMap = databaseSync.getConfigurations();
        List<Configuration> configurationList = configurationMap.get(key);
        List<Configuration> configurations= configurationList.stream().filter(x-> x.isReference() == true).collect(Collectors.toList());

        if (configurations.size() < 1) {
            DetailLogger.info("This setting does not have any sub-setting values. key={}", key);
            return databaseSync;
        }

        List<ConfigurationReference> configurationReferences = importDatabaseByTable(databaseSync.getDatabaseName(), CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE.TABLE());

        for (Configuration configuration : configurations) {

            String code = configuration.getCode();

            List<ConfigurationReference> list = configurationReferences
                                                    .stream().filter(x-> x.getCode().equalsIgnoreCase(code))
                                                    .collect(Collectors.toList());

            if (list.size() > 0) {
                // reference update.
                configurationList.stream().filter(x-> x.getCode().equals(code)).findFirst().ifPresent(target -> target.setChildList(list));
            } else {
                continue;
            }
        }

        databaseSync.setConfigurations(key, configurationList);

        return databaseSync;
    }

    public List<Configuration> loadConfigurations(ConfigValues configValue) {

        if (!CommonsUtil.isEmpty(configValue)) {
            String database = configValue.DATABASE();
            String table = configValue.TABLE();
            String category = configValue.CATEGORY();
            String code = configValue.CODE();

            return getConfiguration(database, table, category, code);
        } else {
            return null;
        }
    }

    public void importReference(ConfigValues configValue, DatabaseSync databaseSync) {}
    /*@Override
    public void importReference(ConfigValues configValue, DatabaseSync databaseSync) {

        if (CommonsUtil.isEmpty(configValue) || CommonsUtil.isEmpty(configValue.DATABASE()) || CommonsUtil.isEmpty(configValue.TABLE())) {
            DetailLogger.error("configValue is Empty, configValue={}", configValue);
            return;
        }

        List<Configuration> configurations = databaseSync.getConfiguration(configValue.DATABASE(), configValue.TABLE());

        if (CommonsUtil.isEmpty(configurations)) {
            DetailLogger.info("configurations is Empty, configurations={}", configurations);
            return;
        }

        for (Configuration configuration : configurations) {

            if (!configValue.CATEGORY().equalsIgnoreCase(configuration.getCategory()) || !configValue.CODE().equalsIgnoreCase(configuration.getCode())) {
                continue;

            } else {

                if (configuration.isReference() == true) {

                    Configuration reference = importDatabaseByTable(configValue.DATABASE(), configValue.TABLE());
                    Configuration toBe = configuration;
                    toBe.setChild(reference);
                    databaseSync.replace(configValue.TABLE(), configuration, toBe);
                    break;
                }
            }
        }
    }*/

}

