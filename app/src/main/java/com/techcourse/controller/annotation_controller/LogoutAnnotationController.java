package com.techcourse.controller.annotation_controller;

import com.techcourse.controller.UserSession;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LogoutAnnotationController {

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView executeGetLogout(final HttpServletRequest req, final HttpServletResponse res) {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        final String viewName = "redirect:/";

        return new ModelAndView(new JspView(viewName));
    }
}
