package com.invitation.module.common.util;

public enum UserValues {

    USER_STATUS_LOCK("L"),
    USER_STATUS_MANAGE("M"),
    USER_STATUS_YES("Y"),
    USER_STATUS_DELETE("D"),
    USER_STATUS_PAUSE("P");

    private String value;

    UserValues(String value) {
        this.value = value;
    }

    public String VALUES() {return value;}
}
