package com.invitation.module.api.service.config;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.rds.service.invitation.configuration.RdsInvitationConfigurationService;
import com.invitation.module.rds.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CacheInvitationDatabaseSync implements DatabaseSync {

    private final RdsInvitationConfigurationService invitationConfigurationService;

    //@Qualifier("RedisConfigurationServiceImpl")
    private final RedisService redisConfigurationServiceImpl;

    @Override
    public Map<String, Date> getLastUpdate() {
        return createCompareData(invitationConfigurationService.getConfigUpdateTime());
    }

    @Override
    public Map<String, Date> createCompareData(List<Configuration> list) {
        Map<String, Date> map = new HashMap<>();

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

        redisConfigurationServiceImpl.save(key, configurations);
        DetailLogger.debug("Update settings gConfigurations={}", configurations);
    }

    @Override
    public Map<String, List<Configuration>> getConfigurations() {

        redisConfigurationServiceImpl.findAll();

        return null;
    }

    @Override
    public List<Configuration> getConfiguration(String table, String code) {

        if (CommonsUtil.isEmpty(table) || CommonsUtil.isEmpty(code)) {
            DetailLogger.error("parameter is null table={}, code={}", table, code);
            return null;
        }

        String key = ConfigValues.CONFIG_DATABASE_INVITATION.DATABASE().concat("-").concat(table.toUpperCase());

        List<Configuration> list = redisConfigurationServiceImpl.findAllById(key);

        if (CommonsUtil.isEmpty(list) || list.size() < 1) {
            DetailLogger.info("불러올 설정값이 없습니다. table={}, key={}", table, key);
            return null;
        }

        return list.stream().filter(x-> x.getCode().equalsIgnoreCase(code)).collect(Collectors.toList());
    }

    @Override
    public String getDatabaseName() {
        return null;
    }

    @Override
    public void replace(String key, Configuration asIs, Configuration toBe) {

    }
}
