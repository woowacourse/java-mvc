package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/test.jsp"));
    }
}
