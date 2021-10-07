package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@nextstep.web.annotation.Controller
public class RegisterViewController implements Controller {

    @Override
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return "/register.jsp";
    }
}
