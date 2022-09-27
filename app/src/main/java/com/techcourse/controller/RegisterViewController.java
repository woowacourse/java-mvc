package com.techcourse.controller;

import static nextstep.web.support.RequestMethod.GET;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;

@Controller
public class RegisterViewController {

    @RequestMapping(value = "/register/view", method = GET)
    public ModelAndView register(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
