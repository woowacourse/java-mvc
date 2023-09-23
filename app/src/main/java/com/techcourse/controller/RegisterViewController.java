package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.RedirectView;

@Controller
public class RegisterViewController {

    @RequestMapping("/register/view")
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new RedirectView("/register.jsp"));
    }
}
