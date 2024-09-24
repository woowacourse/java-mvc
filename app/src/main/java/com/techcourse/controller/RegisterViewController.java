package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;

public class RegisterViewController implements Controller {

    private static final String REGISTER_JSP = "/register.jsp";

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return REGISTER_JSP;
    }
}
