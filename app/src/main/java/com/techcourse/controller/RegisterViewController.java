package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.view.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class RegisterViewController {

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
