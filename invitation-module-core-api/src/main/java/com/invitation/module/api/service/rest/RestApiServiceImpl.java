package com.invitation.module.api.service.rest;


import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.logger.ReqRecvLogger;
import com.invitation.module.common.util.CommonsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class RestApiServiceImpl implements RestApiService {

    private final RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> get(String uri, HttpHeaders httpHeaders, Object body, Class responseType) throws URISyntaxException, NullPointerException {
        return request(uri, HttpMethod.GET, httpHeaders, body, responseType);
    }

    @Override
    public ResponseEntity<?> put(String uri, HttpHeaders httpHeaders, Object body, Class responseType) throws URISyntaxException, NullPointerException {
        return request(uri, HttpMethod.POST, httpHeaders, body, responseType);
    }

    @Override
    public ResponseEntity<?> delete(String uri, HttpHeaders httpHeaders, Object body, Class responseType) throws URISyntaxException, NullPointerException {
        return request(uri, HttpMethod.POST, httpHeaders, body, responseType);
    }

    @Override
    public ResponseEntity<?> post(String uri, HttpHeaders httpHeaders, Object body, Class responseType) throws URISyntaxException, NullPointerException {
        return request(uri, HttpMethod.POST, httpHeaders, body, responseType);
    }


   @Override
    public ResponseEntity request(String uri, HttpMethod method, HttpHeaders httpHeaders, Object body, Class responseType) throws URISyntaxException, NullPointerException {

        HttpHeaders headers = generateHeaders(httpHeaders);
        URI requestUri = getUri(uri);

        RequestEntity requestEntity = new RequestEntity(body, headers, method, requestUri);

        ReqRecvLogger.info("[REQ] uri={}, method={}, headers={}, body={}", uri, method, headers, body);


        ResponseEntity<?> response;
        try {
            response = restTemplate.exchange(uri, method, requestEntity, responseType);
            ReqRecvLogger.info("[RECV] uri={}, method, response={}", uri, method, response.getBody().toString());
        } catch (Exception e) {
            ReqRecvLogger.error("Exception during Rest Api Request, e={}", e.getMessage());
            return null;
        }

        return response;
    }

    @Override
    public HttpHeaders generateHeaders(HttpHeaders httpHeaders) {

        HttpHeaders headers = new HttpHeaders();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        headers.add(HttpHeaders.USER_AGENT, request.getHeader(HttpHeaders.USER_AGENT));
        headers.add(HttpHeaders.CONTENT_TYPE, request.getHeader(HttpHeaders.CONTENT_TYPE));
        headers.add(HttpHeaders.ACCEPT_CHARSET, request.getHeader(HttpHeaders.ACCEPT_CHARSET));
        headers.add(HttpHeaders.ACCEPT, request.getHeader(HttpHeaders.ACCEPT));

        headers.putAll(httpHeaders);

        return headers;
    }

    @Override
    public URI getUri(String uri) throws URISyntaxException {
        if (CommonsUtil.isEmpty(uri)) {
            DetailLogger.error("There is no uri for restapi request.");
            throw new NullPointerException("uri is null");
        }

        return new URI(uri);
    }
}