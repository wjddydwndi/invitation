package com.invitation.module.api.service.util;

import com.invitation.module.api.service.config.ConfigService;
import com.invitation.module.api.service.token.AuthService;
import com.invitation.module.common.logger.DetailLogger;
import static com.invitation.module.common.model.configuration.ConfigValues.*;

import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.model.configuration.ConfigurationReference;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.common.util.Headers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApiSecurityServiceImpl implements ApiSecurityService {

    private final ConfigService configService;

    private final AuthService authService;

    @Value("${server.api.path}")
    private String commonPaths;

    public boolean isPassRequest(HttpServletRequest request, HttpServletResponse response) {
        // block 된 IP 체크
        if (isBlockedIpAddress(request, response) == true) {return false;}
        // SUPER USER
        if (isSuperUser(request, response) == true) {return true;} // 마스터 계정으로 접속할 경우, PASS

        // access-token 체크.
        /*if (isValidToken(request) == false) {
            // Permit Page

            String uri = request.getRequestURI();
            if (isAllowedPage(uri) == true) {
                return true;
            } else {
                return false;
            }
        }*/

        // 차단 계정 체크
        if (isBlockedUser(request, response) == false) {return false;}
        // request uri 체크
        if (isConsistentRequestUri(request, response) == false) {return false;}
        // 동일 아이디로 다중 접속 체크.
        if (isMultipleAccessBySingleUser(request, response) == false) {return false;}

        return true;
    }



    public String getClientIp(HttpServletRequest request) {

        String ip = request.getHeader(Headers.HEADER_X_FORWARDED_FOR.VALUE());

        if (ip == null) {
            ip = request.getHeader(Headers.HEADER_PROXY_CLIENT_IP.VALUE());
        }

        if (ip == null) {
            ip = request.getHeader(Headers.HEADER_WL_PROXY_CLIENT_IP.VALUE());
        }

        if (ip == null) {
            ip = request.getHeader(Headers.HEADER_HTTP_CLIENT_IP.VALUE());
        }

        if (ip == null) {
            ip = request.getHeader(Headers.HEADER_HTTP_X_FORWARDED_FOR.VALUE());
        }

        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        DetailLogger.info("> Result : IP Address : "+ip);

        return ip;
    }

    @Override
    public boolean isSuperUser(HttpServletRequest request, HttpServletResponse response) {
        DetailLogger.info("Check HttpRequestFilter ::: Super user access.");


        String email = String.valueOf(request.getAttribute("email"));

        if (CommonsUtil.isEmpty(email)) {
            return false;
        }

        List<Configuration> configurations = configService.loadConfigurations(CONFIG_INVITATION_TABLE_T_CONFIGURATION_SUPER_USER);

        if (CommonsUtil.isEmpty(configurations)) {
            return false;
        }

        if (isFilterByReference(CONFIG_INVITATION_TABLE_T_CONFIGURATION_SUPER_USER.CODE(), configurations, email) == true) {
            DetailLogger.info("Log in to a master account, email={}", email);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isAllowedPage(HttpServletRequest request) {

        String uri = request.getRequestURI();

        // 허용된 페이지 가져오기.
        List<Configuration> configurations = configService.loadConfigurations(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE_ALLOWED_PAGE);

        if (CommonsUtil.isEmpty(configurations)) {
            return false;
        }

        if (isFilterByReference(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE_ALLOWED_PAGE.CODE(), configurations, uri) == true) {
            return true;
        }

        return false;
    }


    public boolean isBlockedIpAddress(HttpServletRequest request, HttpServletResponse response) {

        String ip = getClientIp(request);

        List<Configuration> configurations = configService.loadConfigurations(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE_BLOCKED_ADDRESS);

        if (CommonsUtil.isEmpty(configurations)) {
            return false;
        }

        if (isFilterByReference(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE_BLOCKED_ADDRESS.CODE(), configurations, ip) == true) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isBlockedUser(HttpServletRequest request, HttpServletResponse response) {

        String email = String.valueOf(request.getAttribute("email"));

        if (CommonsUtil.isEmpty(email)) {
            return false;
        }

        List<Configuration> configurations = configService.loadConfigurations(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE_BLOCKED_USER);

        if (CommonsUtil.isEmpty(configurations)) {
            return false;
        }

        if (isFilterByReference(CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE_BLOCKED_USER.CODE(), configurations, email) == true) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isMultipleAccessBySingleUser(HttpServletRequest request, HttpServletResponse response) {

        // 확인 필요
        // AccessToken check?

        return true;
    }

    public boolean isConsistentRequestUri(HttpServletRequest request, HttpServletResponse response) {

        boolean isConsistent = request.getRequestURI().startsWith(commonPaths);

        if (isConsistent == false) {
            DetailLogger.info("Check HttpRequestFilter ::: {} is not Consistent Request Uri.", request.getRequestURI());
        }

        return isConsistent;
    }

    public boolean isValidToken(HttpServletRequest request) {

        String accessToken = request.getHeader(Headers.HEADER_AUTHORIZATION.VALUE());
        String refreshToken = request.getHeader(Headers.HEADER_AUTHORIZATION_REFRESH.VALUE());
        String userId = request.getHeader(Headers.HEADER_USER_KEY.VALUE());

        boolean isProcess = authService.tokenProcess(userId, accessToken, refreshToken);

        if (isProcess == false) {
            DetailLogger.info("Check HttpRequestFilter ::: {} is not validate token. accessToken={}, refreshToken={}", userId, accessToken, refreshToken);
            return false;
        }

        return true;
    }

    public boolean isFilterByReference(String code, List<Configuration> configurations, String target) {

        if (CommonsUtil.isEmpty(code) || CommonsUtil.isEmpty(configurations) || CommonsUtil.isEmpty(target)) {
            DetailLogger.error("parameter is null taget={}, configurations={}, code={}", target, configurations, code);
            return false;
        }

        for (Configuration configuration : configurations) {
            List<ConfigurationReference> references = (List<ConfigurationReference>)configuration.getChildList();

            if (CommonsUtil.isEmpty(references)) continue;

            for (ConfigurationReference reference : references) {

                if (target.contains(reference.getValue())) {
                    DetailLogger.info("Check {} ::: {} is not allowed.", code, target);
                    return true;
                }
            }
        }
        return false;
    }

    // security 적용시
    /*public Authentication getAuthentication(HttpServletRequest request) {

        String accessToken = request.getHeader(Headers.HEADER_AUTHORIZATION.VALUE());
        String refreshToken = request.getHeader(Headers.HEADER_AUTHORIZATION_REFRESH.VALUE());
        String userId = request.getHeader(Headers.HEADER_USER_KEY.VALUE());

        if (CommonsUtil.isEmpty(accessToken) || CommonsUtil.isEmpty(refreshToken) || CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("parameter is null accessToken={}, refreshToken={}, userId={}", accessToken, refreshToken, userId);
            throw new NullPointerException("There are no parameters to import authentication.");
        }

        return authService.authenticationProcess(userId, accessToken, refreshToken);
    }*/

}
