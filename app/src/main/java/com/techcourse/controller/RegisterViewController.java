package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class RegisterViewController implements Controller {

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
