package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginViewController {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @RequestMapping("/login/view")
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return new ModelAndView(new RedirectView("/index.jsp"));
            })
            .orElse(new ModelAndView(new RedirectView("/login.jsp")));
    }
}
