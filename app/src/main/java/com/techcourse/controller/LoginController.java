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
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        log.info("Method: POST, Request URI: {}", req.getRequestURI());

        if (UserSession.isLoggedIn(req.getSession())) {
            return "redirect:/index.jsp";
        }

        final String account = req.getParameter("account");
        final String password = req.getParameter("password");

        try {
            User user = loginService.login(account, password);

            HttpSession session = req.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);

            return "redirect:/index.jsp";
        } catch (AuthException e) {
            return "redirect:/401.jsp";
        }
    }
}
