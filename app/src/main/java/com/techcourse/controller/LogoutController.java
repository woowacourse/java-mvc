package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(final HttpSession session) {
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}
