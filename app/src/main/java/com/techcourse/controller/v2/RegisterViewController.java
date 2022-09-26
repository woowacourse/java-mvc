package com.techcourse.controller.v2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@nextstep.web.annotation.Controller
public class RegisterViewController {

    @RequestMapping(value = "/v2/register/view", method = RequestMethod.GET)
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return "/register.jsp";
    }
}
