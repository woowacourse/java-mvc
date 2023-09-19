package com.techcourse.controllerv2;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static web.org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class RegisterViewControllerV2 {

    @RequestMapping(value = "/register/view", method = GET)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
