package com.invitation.module.common.model.rest;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class ServerResponse<T> implements Serializable {

    private String code;
    private String message;
    private T data;

    public ServerResponse() {}

    public ServerResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ServerResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
