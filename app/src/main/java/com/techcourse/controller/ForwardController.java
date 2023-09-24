package com.techcourse.controller;

import static web.org.springframework.web.bind.annotation.RequestMethod.GET;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class ForwardController {

    private static final String HOME_JSP = "/index.jsp";

    @RequestMapping(value = "/", method = GET)
    public ModelAndView home(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(HOME_JSP));
    }
}
