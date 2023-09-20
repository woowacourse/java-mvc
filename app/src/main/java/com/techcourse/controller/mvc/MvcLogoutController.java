package com.techcourse.controller.mvc;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class MvcLogoutController {

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String path = execute(req, res);
        return new ModelAndView(new JspView(path));
    }

    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var session = req.getSession();
        session.removeAttribute(MvcUserSession.SESSION_KEY);
        return "redirect:/";
    }
}
