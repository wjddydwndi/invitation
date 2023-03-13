package com.invitation.module.api.filter;

import com.invitation.module.api.service.util.ApiSecurityService;
import com.invitation.module.common.logger.DetailLogger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpRequestFilter implements Filter {


    public HttpRequestFilter() {}

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

/*
        if (apiSecurityFilter.isPassRequest(req, res) == false) {
            return;
        }*/

        // 1. request 파라미터터를 이용해 요청의 필터 작업을 수행한다.
        HttpRequestWrapper wrapper = new HttpRequestWrapper(req);
        byte[] byteArray = wrapper.getByteArray();

        DetailLogger.info("필터 처리 bytes[] = {}", byteArray);

        try {
            // 2. 체인의 다음 필터 처리.
           chain.doFilter(req, res);

        } catch (Exception e) {
            DetailLogger.error("필터 처리 중 Exception 발생 e={}", e.getMessage());
        }

    }

    @Override
    public void destroy() {}
}
