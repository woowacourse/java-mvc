package com.techcourse.controller.regacy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class RegisterViewController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) {
        if (!"GET".equals(req.getMethod())) {
            return "/404.jsp";
        }
        return "/register.jsp";
    }
}
