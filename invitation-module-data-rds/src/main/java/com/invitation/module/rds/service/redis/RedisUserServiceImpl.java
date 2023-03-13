package com.invitation.module.rds.service.redis;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;
import com.invitation.module.common.util.CommonsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisUserServiceImpl implements RedisService<User, String>{

    @Qualifier("redisUserTemplate")
    private final RedisTemplate redisUserTemplate;

    @Override
    public int save(String s, List<User> list) {return 0;}

    @Override
    public int save(User user) {

        if (CommonsUtil.isEmpty(user) || CommonsUtil.isEmpty(user.getId())) {
            DetailLogger.error("Insufficient parameters to query redis. user = {}", user);
            return 0;
        }

        redisUserTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {
                    operations.watch(user.getId());
                    operations.multi();

                    String key = getRedisKey(user.getId());
                    operations.opsForValue().set(key, user);

                } catch (Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    //operations.discard();// rollback
                    return 0;
                }

                operations.exec();
                // operations.unwatch();
                return 1;
            }
        });

        return 0;
    }

    @Override
    public User findById(String userId) {

        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("Insufficient parameters to query redis. userId = {}", userId);
            return null;
        }

        Object object = redisUserTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                List<Token> list = null;
                try {

                    String key = getRedisKey(userId);

                    operations.watch(key);

                    operations.multi();
                    operations.opsForValue().get(key);

                } catch (Exception e) {
                    DetailLogger.error("Redis Exception Occurred. e={}", e.getMessage());
                    return null;
                }

                return operations.exec();
            }
        });

        return (User) object;
    }

    @Override
    public List<User> findAllById(String s) {return null;}

    @Override
    public List<User> findAll() {return null;}

    @Override
    public boolean existsById(String userId) {

        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("Insufficient parameters to query redis. userId = {}", userId);
            return false;
        }

        boolean isExist = false;
        Object object = redisUserTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                try {
                    operations.watch(userId);
                    operations.multi();
                    operations.opsForValue().get(userId);


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
    public int delete(User user) {

        if (CommonsUtil.isEmpty(user)) {
            DetailLogger.error("There are no parameters to delete from Redis. user = {}", user);
            return 0;
        }

        redisUserTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {

                    operations.watch(user);
                    operations.multi();

                    String key = getRedisKey(user.getId());
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
    public int deleteById(String userId) {

        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("There are no parameters to delete from Redis. userId = {}", userId);
            return 0;
        }

        redisUserTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {

                    operations.watch(userId);
                    operations.multi();

                    String key = getRedisKey(userId);
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
    public int deleteAll() {return 0;}

    @Override
    public int deleteAll(List<User> list) {return 0;}

    @Override
    public String getRedisKey(String userId) {
        return RedisValues.REDIS_USER_KEY.VALUE().concat(userId);
    }
}
