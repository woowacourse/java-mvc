package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NotFoundController {

    public String execute(HttpServletRequest req, HttpServletResponse res) {
        return "/404.jsp";
    }
}
