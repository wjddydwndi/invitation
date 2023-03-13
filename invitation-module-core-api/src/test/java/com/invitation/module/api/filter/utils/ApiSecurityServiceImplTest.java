package com.invitation.module.api.filter.utils;

import com.invitation.module.api.service.config.ConfigService;
import com.invitation.module.api.service.config.TestConfiguration;
import com.invitation.module.api.service.util.ApiSecurityService;
import com.invitation.module.common.util.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
@ActiveProfiles("")
@SpringBootTest(classes = TestConfiguration.class)
class ApiSecurityServiceImplTest {

    @Autowired
    private ConfigService configService;

    @Autowired
    private ApiSecurityService apiSecurityFilter;



    @Test
    void test1() {

        configService.loadConfiguration();

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        mockHttpServletRequest.setRequestURI("/login");
        mockHttpServletRequest.setAttribute("email", "test-user");

        String uuid = UUID.randomUUID().toString();
        mockHttpServletRequest.addHeader(Headers.HEADER_AUTHORIZATION.VALUE(), uuid);
        mockHttpServletRequest.addHeader(Headers.HEADER_ACCEPT_LANGUAGE.VALUE(), "ko");


/*        boolean isBlockedIpAddress = apiSecurityFilter.isBlockedIpAddress(mockHttpServletRequest, mockHttpServletResponse);
        boolean isSuperUser = apiSecurityFilter.isSuperUser(mockHttpServletRequest, mockHttpServletResponse);
        boolean isBlockedUser = apiSecurityFilter.isBlockedUser(mockHttpServletRequest, mockHttpServletResponse);
        boolean isValidToken = apiSecurityFilter.isValidToken(mockHttpServletRequest, mockHttpServletResponse);*/

        boolean isPass = apiSecurityFilter.isPassRequest(mockHttpServletRequest, mockHttpServletResponse);
        Assertions.assertTrue(isPass);
    }


}