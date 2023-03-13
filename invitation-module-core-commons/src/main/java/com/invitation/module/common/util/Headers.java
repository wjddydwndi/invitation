package com.invitation.module.common.util;

public enum Headers {
    HEADER_AUTHORIZATION("Authorization"),
    HEADER_AUTHORIZATION_REFRESH("Authorization-Refresh"),
    HEADER_USER_KEY("User-Key"),
    HEADER_USER_AGENT("User-Agent"),
    HEADER_ACCEPT_LANGUAGE("Accept-Language"),
    HEADER_X_FORWARDED_FOR("X-Forwarded-For"),
    HEADER_PROXY_CLIENT_IP("Proxy-Client-IP"),
    HEADER_WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
    HEADER_HTTP_CLIENT_IP("HTTP_Client-IP"),
    HEADER_HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR");

    private String value;

    Headers(String value) {this.value = value;   }

    public String VALUE() {
        return value;
    }
}