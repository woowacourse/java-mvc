package com.techcourse.controller.lagacy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.CustomController;

public class RegisterViewController implements CustomController {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return "/register.jsp";
    }
}
