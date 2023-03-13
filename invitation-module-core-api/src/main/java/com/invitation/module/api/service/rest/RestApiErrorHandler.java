package com.invitation.module.api.service.rest;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.logger.ReqRecvLogger;
import com.invitation.module.common.util.ServerResponseValues;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

/**
 * 기본 오류 처리
 * 기본적으로 RestTemplate은 HTTP 오류의 경우 다음 예외 중 하나를 발생시킨다.
 * 1. HttpClientErrorException : Http 상태 4xx인 경우
 * 2. HttpServerErrorException : Http 상태 5xx인 경우
 * 3. UnknownHttpStatusCodeErrorException : 알 수 없는 Http 상태인 경우
 *
 * 이 모든 예외는 RestClientResponseException의 확장이다.
 *
 * **/
public class RestApiErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() != HttpStatus.Series.SUCCESSFUL;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        for (ServerResponseValues srv: ServerResponseValues.values()) {
            if (srv.CODE().equals(response.getStatusCode())) {
                ReqRecvLogger.error("[RECV] exception code={}, value={}", srv.CODE(), srv.VALUE());
            }
        }
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ResponseErrorHandler.super.handleError(url, method, response);
    }
}
