package com.techcourse.controller;

import static web.org.springframework.web.bind.annotation.RequestMethod.GET;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout", method = GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        final var session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}
