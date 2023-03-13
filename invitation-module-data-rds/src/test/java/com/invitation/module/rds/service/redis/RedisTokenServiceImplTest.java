package com.invitation.module.rds.service.redis;

import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;
import com.invitation.module.common.service.token.TokenProvider;
import com.invitation.module.common.service.token.TokenProviderImpl;
import com.invitation.module.common.service.token.TokenService;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.rds.config.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = TestConfiguration.class)
class RedisTokenServiceImplTest {

    @Autowired RedisService redisTokenServiceImpl;
    @Autowired TokenProvider tokenProvider;
    @Autowired TokenService tokenService;

    @Value("${token.access-expiration}")
    String accessTokenExpiration;

    @Test
    void save() {
        /**
         * jwt example
         * access-token
         * token-type :
         *
         * **/


        //String userId = UUID.randomUUID().toString();
        String userId = "ceedc5f6-4707-4799-8ce5-5aa96a2f9b41";
        String email = "hj.jeong@sbicosmoney.com";
        User user = new User();
        user.setId(userId);
        user.setEmail(email);
        long expiration = Long.parseLong(accessTokenExpiration);

        Token token = tokenService.generateToken(user);


        //String token = tokenProvider.generateToken(user, expiration);


        redisTokenServiceImpl.save(token);
        List<Token> list = (List<Token>) redisTokenServiceImpl.findById(token.getUserId());
        Token t = list.get(0);


    }

}