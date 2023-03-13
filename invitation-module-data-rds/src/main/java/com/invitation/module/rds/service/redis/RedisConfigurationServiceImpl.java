package com.invitation.module.rds.service.redis;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.util.CommonsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisConfigurationServiceImpl  implements RedisService<Configuration, String>{
    private final RedisTemplate redisConfigurationTemplate;

    @Override
    public int save(String key, List<Configuration> configurations) {
        if (CommonsUtil.isEmpty(configurations) || configurations.size() < 1) {
            DetailLogger.error("Insufficient parameters to query redis. configurations={}", configurations);
            return 0;
        }

        redisConfigurationTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {
                    operations.watch(key);

                    operations.multi();

                    String redisKey = getRedisKey(key);
                    operations.opsForValue().set(redisKey, configurations);

/*                    if (existsById(redisKey) == false) {
                        operations.discard();
                        return 0;
                    }*/

                } catch (Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    //operations.discard();// rollback
                    return 0;
                }

                operations.exec();
                operations.unwatch();
                return 1;
            }
        });

        return 0;
    }

    @Override
    public int save(Configuration configuration) {

        if (CommonsUtil.isEmpty(configuration) || CommonsUtil.isEmpty(configuration.getDatabaseName()) || CommonsUtil.isEmpty(configuration.getTableName())) {
            DetailLogger.error("Insufficient parameters to query redis. configuration={}", configuration);
            return 0;
        }

        redisConfigurationTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {
                    operations.watch(configuration);

                    operations.multi();

                    String key = getRedisKey(CommonsUtil.createConfigurationKey(configuration));
                    operations.opsForValue().set(key, configuration);

                    if (existsById(key) == false) {
                        operations.discard();
                        return 0;
                    }

                } catch (Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    operations.discard();// rollback
                    return 0;
                }

                operations.exec();
                operations.unwatch();
                return 1;
            }
        });

        return 0;
    }

    @Override
    public Configuration findById(String key) {

        if (CommonsUtil.isEmpty(key)) {
            DetailLogger.error("Insufficient parameters to query redis. key = {}", key);
            return null;
        }

        Object object = redisConfigurationTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                Configuration configuration = null;

                String redisKey = getRedisKey(key);

                try {
                    operations.watch(redisKey);

                    operations.multi();
                    operations.opsForValue().get(redisKey);

                } catch (Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    return null;
                }

                return operations.exec();
            }
        });

        return (Configuration) object;
    }

    @Override
    public List<Configuration> findAllById(String key) {
        if (CommonsUtil.isEmpty(key)) {
            DetailLogger.error("Insufficient parameters to query redis. key = {}", key);
            return null;
        }

        Object object = redisConfigurationTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                Configuration configuration = null;

                String redisKey = getRedisKey(key);

                try {
                    operations.watch(redisKey);

                    operations.multi();
                    operations.opsForValue().get(redisKey);

                } catch (Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    return null;
                }

                return operations.exec();
            }
        });

        List<List<Configuration>> list = (List<List<Configuration>>) object;

        List<Configuration> result = new ArrayList<>();

        if (list.size()  > 0 ) {
            result = list.get(0);
        }

        return result;
    }

    @Override
    public List<Configuration> findAll() {

        redisConfigurationTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                Configuration configuration = null;
                try {

                    operations.multi();
                    ListOperations<String, Object> listOps = (ListOperations<String, Object>) operations.opsForList().getOperations();

                    listOps.size("");


                } catch (Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    return null;
                }

                operations.exec();
                operations.unwatch();
                return configuration;
            }
        });

        return null;
    }

    @Override
    public boolean existsById(String key) {

        if (CommonsUtil.isEmpty(key)) {
            DetailLogger.error("Insufficient parameters to query redis. key={}", key);
            return false;
        }

        boolean isExist = false;
        Object object = redisConfigurationTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                try {
                    operations.watch(key);
                    operations.multi();
                    operations.opsForValue().get(key);

                } catch(Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    return null;
                }

                return operations.exec();
            }
        });

        if (!CommonsUtil.isEmpty(object)) {
            isExist = true;
        }

        return isExist;
    }

    @Override
    public int delete(Configuration configuration) {

        if (CommonsUtil.isEmpty(configuration)) {
            DetailLogger.error("There are no parameters to delete from Redis. configuration={}", configuration);
            return 0;
        }

        redisConfigurationTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {

                    operations.watch(configuration);
                    operations.multi();

                    String key = getRedisKey(CommonsUtil.createConfigurationKey(configuration));
                    operations.delete(key);

                    if (existsById(key) == true) {
                        operations.discard();
                        return 0;
                    }

                } catch (Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    operations.discard();
                    return 0;

                }
                operations.exec();
                operations.unwatch();
                return 1;
            }
        });

        return 0;
    }

    @Override
    public int deleteById(String key) {

        if (CommonsUtil.isEmpty(key)) {
            DetailLogger.error("There are no parameters to delete from Redis. key={}", key);
            return 0;
        }

        redisConfigurationTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {

                    operations.watch(key);
                    operations.multi();

                    String redisKey = getRedisKey(key);
                    operations.delete(redisKey);

                    if (existsById(redisKey) == true) {
                        operations.discard();
                        return 0;
                    }

                } catch (Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    operations.discard();
                    return 0;

                }
                operations.exec();
                operations.unwatch();
                return 1;
            }
        });

        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public int deleteAll(List<Configuration> list) {
        return 0;
    }

    @Override
    public String getRedisKey(String key) {
        return RedisValues.REDIS_CONFIGURATION_KEY.VALUE().concat(key);
    }
}
