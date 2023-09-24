package com.techcourse.controller.legacy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class RegisterViewController implements Controller {

    @Override
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
