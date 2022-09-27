package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;

@Controller
public class RegisterViewController {

    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
