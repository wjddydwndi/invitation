package com.invitation.module.rds.service.scheduler;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.util.BeanUtils;
import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.util.CommonsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
@RequiredArgsConstructor
@Transactional
public class RdsSchedulerConfigurationServiceImpl implements RdsSchedulerConfigurationService {

    @Override
    public <T>T importConfigurationByDatabaseV2(String database, String table) {

        if (CommonsUtil.isEmpty(database) || CommonsUtil.isEmpty(table)) {
            DetailLogger.error("Pass database configuration settings : repository={}, method={}", database, table);
        }

        DetailLogger.debug("Detach key from configurations databaseName={}, tableName={}", database, table);

        try {
            for (ConfigValues config : ConfigValues.values()) {

                if (CommonsUtil.isEmpty(config.DATABASE()) || CommonsUtil.isEmpty(config.TABLE()) || CommonsUtil.isEmpty(config.REPOSITORY()) || CommonsUtil.isEmpty(config.METHOD())) {
                    continue;
                }

                if (config.DATABASE().equalsIgnoreCase(database) && config.TABLE().equalsIgnoreCase(table)) {

                    Class<?> cls = Class.forName(config.REPOSITORY());
                    Object obj = BeanUtils.getBean(cls);

                    Field[] fields = obj.getClass().getDeclaredFields();

                    for (Field field : fields) {
                        field.setAccessible(true);
                    }
                    Method method = obj.getClass().getDeclaredMethod(config.METHOD());

                    return (T) method.invoke(obj);
                }
            }
        } catch (NoSuchMethodException | NoSuchMethodError noSuchMethodException) {
            DetailLogger.error("The method was not found. e={}", noSuchMethodException.getMessage());
        } catch (InvocationTargetException invocationTargetException) {
            DetailLogger.error("invoke() Exception within called method e={}", invocationTargetException.getMessage());
        } catch(IllegalAccessException illegalAccessException) {
            DetailLogger.error("Check the parameters when calling the method e={}", illegalAccessException.getMessage());
        } catch(ClassNotFoundException classNotFoundException) {
            DetailLogger.error("Class not found. e={}", classNotFoundException.getMessage());
        } catch (Exception e) {
            DetailLogger.error("Exception while importing data e={}", e.getMessage());
        }

        return null;
    }
}
