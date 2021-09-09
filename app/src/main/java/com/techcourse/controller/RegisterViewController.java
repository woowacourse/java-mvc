package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;

public class RegisterViewController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "/register.jsp";
    }
}
