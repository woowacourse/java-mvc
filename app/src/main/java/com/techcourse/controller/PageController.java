package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.view.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.view.JspView;

@Controller
public class PageController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView("/index.jsp"));
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public ModelAndView notFoundError(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView("/404.jsp"));
    }
}
