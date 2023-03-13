/*
package com.invitation.module.api.config;

import com.invitation.module.api.filter.TokenAuthenticationFilter;
import com.invitation.module.api.service.util.ApiSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ApiSecurityService apiSecurityService;

    // 정적 자원에 대해서는 security를 적용하지 않음.
    public  void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    protected  void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()  // rest api -> 기본 설정 disable
                .csrf().disable()   // csrf -> 비활성화, security 설정 시 기본값으로 활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 활용 안함

                .and()
                .authorizeRequests()
                    .antMatchers("/apis/user/login", "/apis/user/signup").permitAll()// 회원가입, 로그인에 대한 권한
                    .anyRequest().hasRole("USER_ROLE")// 나머지 요청에 대해서는 USER_ROLE을 가져야만 접근 가능.

                .and()
                .addFilterBefore(new TokenAuthenticationFilter(apiSecurityService), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(authenticationManager(), apiSecurityService);
        tokenAuthenticationFilter.setFilterProcessesUrl("/apis/user/login");
    }

}
*/
