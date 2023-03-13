package com.invitation.module.api.config;

import com.invitation.module.api.service.rest.RestApiErrorHandler;
import com.invitation.module.api.service.util.ApiSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    @Value("${spring.rest.connection-timeout}")
    private int connectionTimeout;

    @Value("${spring.rest.read-timeout}")
    private int readTimeout;

    private final ApiSecurityService apiSecurityService;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder.requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .errorHandler(new RestApiErrorHandler())
                .build();
    }
}
