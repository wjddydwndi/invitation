package com.invitation.module.rds.service.redis;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.util.CommonsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenServiceImpl implements RedisService<Token, String>{
    @Qualifier("redisTokenTemplate")
    private final RedisTemplate redisTokenTemplate;

    @Override
    public int save(String s, List<Token> list) {return 0;}

    @Override
    public int save(Token token) {

        if (CommonsUtil.isEmpty(token) || CommonsUtil.isEmpty(token.getUserId())) {
            DetailLogger.error("Insufficient parameters to query redis. token = {}", token);
            return 0;
        }

        redisTokenTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {
                    operations.watch(token.getUserId());
                    operations.multi();

                    String key = getRedisKey(token.getUserId());
                    operations.opsForValue().set(key, token);

                    /*if (existsById(key) == true) {
                        operations.discard();
                        return 0;
                    }*/

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
    public Token findById(String userId) {

        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("Insufficient parameters to query redis. userId = {}", userId);
            return null;
        }

        Object object = redisTokenTemplate.execute(new SessionCallback() {
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

        return (Token) object;
    }

    @Override
    public List<Token> findAllById(String s) {return null;}

    @Override
    public List<Token> findAll() {return null;}

    @Override
    public boolean existsById(String userId) {

        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("Insufficient parameters to query redis. userId = {}", userId);
            return false;
        }

        boolean isExist = false;
        Object object = redisTokenTemplate.execute(new SessionCallback() {
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
    public int delete(Token token) {

        if (CommonsUtil.isEmpty(token)) {
            DetailLogger.error("There are no parameters to delete from Redis. token = {}", token);
            return 0;
        }

        redisTokenTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                try {

                    operations.watch(token);
                    operations.multi();

                    String key = getRedisKey(token.getUserId());
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

        redisTokenTemplate.execute(new SessionCallback() {
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
    public int deleteAll(List<Token> list) {return 0;}

    @Override
    public String getRedisKey(String userId) {
        return RedisValues.REDIS_TOKEN_KEY.VALUE().concat(userId);
    }
}
