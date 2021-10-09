package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@nextstep.web.annotation.Controller
public class RegisterViewController implements Controller {

    @Override
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
