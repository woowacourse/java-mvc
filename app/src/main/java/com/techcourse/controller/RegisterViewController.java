package com.techcourse.controller;

import com.interface21.webmvc.servlet.view.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.controller.Controller;

public class RegisterViewController implements Controller {

    @Override
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
