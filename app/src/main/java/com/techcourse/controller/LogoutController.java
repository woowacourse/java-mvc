package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LogoutController {
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String SLASH = "/";

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(final HttpServletRequest request, final HttpServletResponse response) {
        final var session = request.getSession();
        if (session != null) {
        session.removeAttribute(UserSession.SESSION_KEY);
        }
        return new ModelAndView(new JspView(REDIRECT_PREFIX + SLASH));
    }
}
