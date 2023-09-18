package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class RegisterViewController {

    private static final Logger log = LoggerFactory.getLogger(RegisterViewController.class);

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) {
        log.info("RegisterViewController GET method");

        return new ModelAndView(new JspView("/register.jsp"));
    }
}
