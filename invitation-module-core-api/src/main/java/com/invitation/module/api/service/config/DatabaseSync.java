package com.invitation.module.api.service.config;

import com.invitation.module.common.model.configuration.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DatabaseSync {

    Map<String, Date> getLastUpdate();
    Map<String, Date> createCompareData(List<Configuration> list);
    void setConfigurations(String key, List<Configuration> configurations);
    Map<String, List<Configuration>> getConfigurations();
    List<Configuration> getConfiguration(String table, String code);
    String getDatabaseName();
    void replace(String key, Configuration asIs, Configuration toBe);

}
