package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.view.JspView;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public ModelAndView logout(final HttpServletRequest request, final HttpServletResponse ignored) {
        final HttpSession session = request.getSession();

        session.removeAttribute(UserSession.SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }
}
