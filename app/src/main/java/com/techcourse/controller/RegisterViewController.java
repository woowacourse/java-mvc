package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class RegisterViewController {

    private static final JspView REGISTER_PAGE_VIEW = new JspView("/register.jsp");

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView showRegisterPage(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(REGISTER_PAGE_VIEW);
    }
}
