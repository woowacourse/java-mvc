package com.techcourse.controllerv2;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.GetMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class RegisterViewControllerV2 {

    @GetMapping("/register/view")
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
