package com.invitation.module.api.service.config;

public enum ValueUtil {

    VALUE_YES_Y("YES", "Y"),
    VALUE_NO_N("NO", "N");

    private String code;
    private String value;

    ValueUtil(String code, String value) {
        this.code = code;
        this.value = value;
    }

    String CODE() {return code;}
    String VALUE() {return value;}

}
