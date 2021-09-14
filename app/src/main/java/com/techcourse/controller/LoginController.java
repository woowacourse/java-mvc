package com.techcourse.controller;

import com.techcourse.controller.request.LoginRequest;
import com.techcourse.exception.UnauthorizedException;
import com.techcourse.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Controller {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return "redirect:/index.jsp";
        }

        try {
            LoginRequest loginRequest = getLoginRequest(request);
            loginService.login(loginRequest);

            LOG.debug("Login Success!!");

            return "redirect:/index.jsp";
        } catch (UnauthorizedException e) {
            LOG.debug("Login Failed...");

            return "redirect:/401.jsp";
        }
    }

    private LoginRequest getLoginRequest(HttpServletRequest request) {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        HttpSession httpSession = request.getSession();

        LOG.debug("Login Request => account: {}", account);

        return new LoginRequest(account, password, httpSession);
    }
}
