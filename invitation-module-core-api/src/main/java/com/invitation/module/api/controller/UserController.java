package com.invitation.module.api.controller;

import com.invitation.module.api.service.user.UserService;
import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;
import com.invitation.module.common.model.rest.ServerResponse;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.common.util.exception.TokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.invitation.module.common.util.ServerResponseValues.*;

@RestController
@RequestMapping("/apis/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse login(@RequestBody(required = false) User user, HttpServletRequest request, HttpServletResponse response) throws TokenInvalidException {
        DetailLogger.info("로그인 요청");
        User user1 = new User();
        user1.setEmail("hj.jeong@sbicosmoney.com");
        user1.setPassword("1qw2wa!!@@");
        return userService.login(user1);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ServerResponse signup(@RequestBody(required = false) User user, HttpServletRequest request, HttpServletResponse response) throws TokenInvalidException {
        DetailLogger.info("회원가입 요청");
        return userService.login(user);
    }
}
