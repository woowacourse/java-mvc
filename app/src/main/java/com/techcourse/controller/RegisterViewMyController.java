package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.MyController;
import webmvc.org.springframework.web.servlet.view.JspView;

public class RegisterViewMyController implements MyController {

    @Override
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
