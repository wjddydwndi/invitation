package com.invitation.module.api.config;

import com.invitation.module.api.commons.ApiRequestInterceptor;
import com.invitation.module.api.filter.HttpRequestFilter;
import com.invitation.module.api.service.util.ApiSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.Filter;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final ApiSecurityService apiSecurityService;

    @Bean
    public Filter httpRequestWrapperFilter() {
        return new HttpRequestFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(httpRequestWrapperFilter());
        filterRegistrationBean.addUrlPatterns("/apis/*"); // 특정 URL 포함.
        //  filterRegistrationBean.setUrlPatterns(Arrays.asList("/apis/*", ...)); // 여러 특정 URL 포함
        //  filterRegistrationBean.setOrder();
        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiRequestInterceptor(apiSecurityService))
                .addPathPatterns("/apis/**");
                //.excludePathPatterns("");
    }

    /** Http 헤더의 Accept-Language에 의해 선택
     * setLocale은 지원되지 않음. 브라우저의 설정에 의해서만 설정
     * SessionLocaleResolver : 처음 들어갈 때에는 AcceptHeaderLocaleResolver 처럼 브라우저의 언어 설정에 의한 Accept-Language로 값이 결정 cf) setDefaultLocale 을 설정한다면 해당 기본값이 최우선이다.
     * 세션으로 저장되며, SessionLocaleResolver.LOCALE라는 세션 속성이름으로 클래스를 시리얼라이징해서 저장
     *
     * CookieLocaleResolver : SessionLocaleResolver와 속성은 동일하지만, lang 값이 바뀔 경우, 세션이 아닌 쿠키에 저장
     * 그렇기 때문에 session의 경우 session이 끊어지면 언어 설정이 되돌아오지만, CookieLocaleResolver의 경우 쿠키의 값을 우선으로 불러옴.
     *
     * LocaleResolver.setDefaultLocale : setDefaultLocale로 설정하면 Accept-Language 보다 더 높은 우선순위에
     * **/
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        cookieLocaleResolver.setCookieMaxAge(-1);
        cookieLocaleResolver.setCookiePath("/");
        cookieLocaleResolver.setCookieName("invitation_locale");
        cookieLocaleResolver.setCookieHttpOnly(true);
        cookieLocaleResolver.setCookieSecure(true);
        return cookieLocaleResolver;
    }

    /**
     * 기본적으로 LocaleResolver bean이 설정되어있어야 한다.
     * url 뒤에 특정 locale 인자값을 넘겨 변경하는 방법이다.**/
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /** 어플리케이션 실행중 message source를 reload하여 변경사항을 저장할 수 있다.
     *setCacheSeconds(n) : n초까지 캐싱하고 3초 이후에는 메시지를 다시 읽음.
     * */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("classpath:messages/messages");
        messageSource.setCacheSeconds(3);
        return messageSource;
    }

    /** MessageSource 내부에 변수를 가지고 있으면서 기능을 확장하여 사용자가 MessageSource 사용할 수 있도록 구현된 클래스
     * 확장 기능은 크게 DefaultLocale을 멤버변수로 가지고 있어, MessageSource message를 가져올 떄 Locale에 해당하는 값의 메시지를 가져온다.
     * messageSource에 해당하는 코드의 메시지가 없을 경우 ""을 전달하여 NullPointerException을 방지한다.
     * */
    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource, Locale.ENGLISH);
    }
}

