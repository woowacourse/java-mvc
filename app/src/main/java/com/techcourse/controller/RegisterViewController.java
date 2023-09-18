package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class RegisterViewController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) {
        return "/register.jsp";
    }
}
