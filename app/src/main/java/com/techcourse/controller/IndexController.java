package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class IndexController {

    @RequestMapping(value = "/")
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
