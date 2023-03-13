package com.invitation.module.common.util;

public enum Authority {

    // 사용자 권한
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUPER("ROLE_SUPER");


    private String name;

    Authority(String name) {this.name = name;}

    public String Authority() {
        return name;
    }
}
