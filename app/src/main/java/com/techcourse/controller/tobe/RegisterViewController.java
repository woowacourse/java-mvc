package com.techcourse.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class RegisterViewController {

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterView(final HttpServletRequest req, final HttpServletResponse response) {
        return "register.jsp";
    }
}
