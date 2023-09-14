package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.MyController;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class RegisterViewController implements MyController {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res)  {
        return "/register.jsp";
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest req, HttpServletResponse res) {
        String path = execute(req, res);
        return new ModelAndView(new JspView(path));
    }
}
