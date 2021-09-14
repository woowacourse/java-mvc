package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.AuthException;
import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: POST, Request URI: {}", request.getRequestURI());

        if (UserSession.isLoggedIn(request.getSession())) {
            return "redirect:/index.jsp";
        }

        final String account = request.getParameter("account");
        final String password = request.getParameter("password");

        try {
            User user = loginService.login(account, password);

            HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);

            return "redirect:/index.jsp";
        } catch (AuthException e) {
            return "redirect:/401.jsp";
        }
    }
}
