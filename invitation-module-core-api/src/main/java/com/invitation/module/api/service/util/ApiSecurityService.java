package com.invitation.module.api.service.util;

import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.model.configuration.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ApiSecurityService {

    boolean isPassRequest(HttpServletRequest request, HttpServletResponse response);

    String getClientIp(HttpServletRequest request);

    boolean isSuperUser(HttpServletRequest request, HttpServletResponse response);

    boolean isAllowedPage(HttpServletRequest request);

    boolean isBlockedIpAddress(HttpServletRequest request, HttpServletResponse response);

    boolean isBlockedUser(HttpServletRequest request, HttpServletResponse response);

    boolean isMultipleAccessBySingleUser(HttpServletRequest request, HttpServletResponse response);

    boolean isConsistentRequestUri(HttpServletRequest request, HttpServletResponse response);

    boolean isValidToken(HttpServletRequest request);

    boolean isFilterByReference(String code, List<Configuration> configurations, String target);

}
