package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.mvc.controller.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginViewController implements Controller {

    private static final Logger LOG = LoggerFactory.getLogger(LoginViewController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            User user = UserSession.getUser(request.getSession());
            LOG.info("logged in {}", user.getAccount());
            return "redirect:/index.jsp";
        }

        return "/login.jsp";
    }
}
