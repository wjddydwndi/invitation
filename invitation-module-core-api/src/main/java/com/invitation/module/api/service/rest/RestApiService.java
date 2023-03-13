package com.invitation.module.api.service.rest;

import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;

public interface RestApiService<T> {

    ResponseEntity<?> get(String uri, HttpHeaders httpHeaders, Object body, Class<T> responseType) throws URISyntaxException;
    ResponseEntity<?> put(String uri, HttpHeaders httpHeaders, Object body, Class<T> responseType) throws URISyntaxException;
    ResponseEntity<?> delete(String uri, HttpHeaders httpHeaders, Object body, Class<T> responseType) throws URISyntaxException;
    ResponseEntity<?> post(String uri, HttpHeaders httpHeaders, Object body, Class<T> responseType) throws URISyntaxException;

    ResponseEntity request(String uri, HttpMethod method, HttpHeaders httpHeaders, Object body, Class<T> responseType) throws URISyntaxException, NullPointerException;

    HttpHeaders generateHeaders(HttpHeaders httpHeaders);
    URI getUri(String uri) throws URISyntaxException;
}
