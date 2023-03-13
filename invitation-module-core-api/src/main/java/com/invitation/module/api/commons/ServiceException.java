package com.invitation.module.api.commons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException {

    private String code;
    private String msg;
    private String detail;

    public ServiceException(Exception ex) {super(ex);}

    public ServiceException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(String code, String msg, String detail) {
        super();
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }
}
