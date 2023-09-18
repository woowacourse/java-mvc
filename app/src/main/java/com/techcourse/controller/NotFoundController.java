package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class NotFoundController implements Controller {

    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/404.jsp";
    }
}
