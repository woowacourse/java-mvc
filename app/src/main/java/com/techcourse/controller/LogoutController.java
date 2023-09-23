package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.GetMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static com.techcourse.controller.UserSession.SESSION_KEY;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        request.getSession().removeAttribute(SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }
}
