package com.invitation.module.rds.service.redis;

public enum RedisValues {
    REDIS_TOKEN_KEY("token:"),
    REDIS_USER_KEY("user:"),
    REDIS_CONFIGURATION_KEY("configuration:");

    private String value;

    RedisValues(String value) {
        this.value = value;
    }

    String VALUE() {return value;}
}
