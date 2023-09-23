package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.view.JspView;

@Controller
public class LoginViewController {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView findLoginForm(final HttpServletRequest request, final HttpServletResponse ignored) {
        final String viewName = UserSession.getUserFrom(request.getSession())
                                           .map(user -> {
                                               log.info("logged in {}", user.getAccount());
                                               return "redirect:/index.jsp";
                                           })
                                           .orElse("/login.jsp");

        return new ModelAndView(new JspView(viewName));
    }
}
