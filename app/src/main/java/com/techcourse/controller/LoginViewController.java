package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LoginViewController {

    private static final View LOGIN_PAGE_VIEW = new JspView("redirect:/login.jsp");
    private static final JspView REDIRECT_INDEX_PAGE_VIEW = new JspView("redirect:/index.jsp");

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView showLoginPage(final HttpServletRequest req, final HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return new ModelAndView(REDIRECT_INDEX_PAGE_VIEW);
            })
            .orElse(new ModelAndView(LOGIN_PAGE_VIEW));
    }
}
