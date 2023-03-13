package com.invitation.module.api.commons;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.rest.ServerResponse;

import com.invitation.module.common.util.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static com.invitation.module.common.util.ServerResponseValues.*;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ServerResponse serviceExceptionHandler(ServiceException ex) {
        DetailLogger.error("Exception 예외 발생: code={}, message={}, detail={}", ex.getCode(), ex.getMsg(), ex.getDetail());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setCode(ex.getCode());
        serverResponse.setMessage(ex.getMessage());

        Map cause = new HashMap<String, String>();
        cause.put("err", ex.getDetail());
        serverResponse.setData(cause);

        return serverResponse;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ServerResponse exceptionHandler(Exception ex) {

        DetailLogger.error("Exception 예외 발생: 기타 예외가 발생하여 요청처리가 중단되었다. ={}", ex.getMessage());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setCode(SVR_ERR_CODE_405.CODE());
        serverResponse.setMessage("Exception Occured");

        Map cause = new HashMap<String, String>();
        cause.put("msg", ex.getMessage());
        serverResponse.setData(cause);

        return serverResponse;
    }

    @ExceptionHandler(value = TokenExpirationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ServerResponse tokenExpirationExceptionHandler(Exception ex) {

        DetailLogger.error("Exception 예외 발생: 토큰 만료 ={}", ex.getMessage());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setCode(SVR_ERR_CODE_403.CODE());
        serverResponse.setMessage("토큰 만료 예외 발생");

        Map cause = new HashMap<String, String>();
        cause.put("msg", ex.getMessage());
        serverResponse.setData(cause);

        return serverResponse;
    }

    @ExceptionHandler(value = TokenInvalidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ServerResponse tokenInvalidExceptionHandler(Exception ex) {

        DetailLogger.error("Exception 예외 발생: 유효하지 않은 토큰 ={}", ex.getMessage());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setCode(SVR_ERR_CODE_403.CODE());
        serverResponse.setMessage("유효하지 않은 토큰 예외 발생");

        Map cause = new HashMap<String, String>();
        cause.put("msg", ex.getMessage());
        serverResponse.setData(cause);

        return serverResponse;
    }

    @ExceptionHandler(value = DuplicatedKeyException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ServerResponse duplicatedKeyExceptionHandler(Exception ex) {

        DetailLogger.error("Exception 예외 발생: 중복된 키 값 ={}", ex.getMessage());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setCode(SVR_ERR_CODE_403.CODE());
        serverResponse.setMessage("중복된 키 값 예외 발생");

        Map cause = new HashMap<String, String>();
        cause.put("msg", ex.getMessage());
        serverResponse.setData(cause);

        return serverResponse;
    }

    @ExceptionHandler(value = InvalidParamException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ServerResponse invalidParamExceptionHandler(Exception ex) {

        DetailLogger.error("Exception 예외 발생: 파라미터가 유효하지 않습니다. ={}", ex.getMessage());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setCode(SVR_ERR_CODE_403.CODE());
        serverResponse.setMessage("중복된 키 값 예외 발생");

        Map cause = new HashMap<String, String>();
        cause.put("msg", ex.getMessage());
        serverResponse.setData(cause);

        return serverResponse;
    }


    @ExceptionHandler(value = UnsufficientParamException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ServerResponse unSufficientParamExceptionHandler(Exception ex) {

        DetailLogger.error("Exception 예외 발생: 불충분한 파라미터. ={}", ex.getMessage());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setCode(SVR_ERR_CODE_400.CODE());
        serverResponse.setMessage("불충분한 파라미터");

        Map cause = new HashMap<String, String>();
        cause.put("msg", ex.getMessage());
        serverResponse.setData(cause);

        return serverResponse;
    }
}
