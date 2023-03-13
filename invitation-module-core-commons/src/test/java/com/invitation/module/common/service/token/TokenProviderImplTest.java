package com.invitation.module.common.service.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenProviderImplTest {

    @Autowired
    TokenProvider tokenProvider;

    @Value(value = "${token.access-expiration}")
    private long accessTokenDuration;

    @Value(value = "${token.refresh-expiration}")
    private long refreshTokenDuration;

}