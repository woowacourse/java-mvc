package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class DazzleController {

    @RequestMapping(value = "/dazzle", method = RequestMethod.GET)
    public ModelAndView showMyDazzle(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView("404.jsp"));
    }
}
