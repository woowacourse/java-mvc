package com.techcourse.controller.mvc.view;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class MvcRegisterViewController {

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }

}
