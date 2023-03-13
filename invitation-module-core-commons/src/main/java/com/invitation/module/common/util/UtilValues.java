package com.invitation.module.common.util;

public enum UtilValues {

    INSTANCE_SHA_256("SHA-256"),
    INSTANCE_AES_256_CBC_PKCS5PADDING("AES/CBC/PKCS5Padding"),
    STR_AES("AES"),
    ENCODING_UTF_8("UTF-8");


    private String code;

    UtilValues(String code) {
        this.code = code;
    }

    public String CODE() {return code;}
}
