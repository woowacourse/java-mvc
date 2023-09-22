package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.Controller;

public class LoginViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
    }
}
