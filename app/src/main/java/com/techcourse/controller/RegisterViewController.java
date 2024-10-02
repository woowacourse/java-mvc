package com.techcourse.controller;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterViewController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) {
        return "/register.jsp";
    }
}
