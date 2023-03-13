package com.invitation.module.api.commons;

import com.invitation.module.api.service.util.ApiSecurityService;
import com.invitation.module.common.logger.ReqRecvLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class ApiRequestInterceptor implements HandlerInterceptor {

    private final ApiSecurityService apiSecurityService;

    // 컨트롤러 실행 직전에 동작
    // 반환 값이 true일 경우, 정상적인 진행이 되고, false일 경우 실행이 멈춘다.(컨트롤러로 진입하지 않는다.)
    // 전달인자 중 Object handler는 핸들러 매핑이 찾은 컨트롤러 클래스 객체이다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (isValidateRequest(request) == false) {
            ReqRecvLogger.info("reqeust is Invalid");
            return false;
        }

        return true;
    }

    // 컨트롤러 진입 후 view가 랜더링 되기 전 수행이 된다.
    // 전달인자 ModelAndView를 통해 화면 단에 들어가는 데이터 등의 조작이 가능
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


    // 컨트롤러 진입 후 view가 정상적으로 랜더링 된 후 제일 마지막 에 실행이 되는 메서드
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    public boolean isValidateRequest(HttpServletRequest request) {

        if (apiSecurityService.isValidToken(request) == false) {
            if (apiSecurityService.isAllowedPage(request) == false) {
                return false;
            }
        }
        return true;
    }
}
