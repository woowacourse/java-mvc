package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.mvc.controller.Controller;

public class RegisterViewController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return "/register.jsp";
    }
}
