package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.UnAuthorizedException;
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
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            return "redirect:/index.jsp";
        }
        final String requestAccount = req.getParameter("account");
        final String requestPassword = req.getParameter("password");
        log.info("로그인 요청 => account : {}, password: {}", requestAccount, requestPassword);
        try {
            final User user = loginService.login(requestAccount, requestPassword);
            final HttpSession session = req.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        } catch (UnAuthorizedException e) {
            return "redirect:/401.jsp";
        }
    }
}
