package com.techcourse.controllerv2;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.GetMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LoginViewControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(LoginViewControllerV2.class);

    @GetMapping("/login/view")
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (UserSessionV2.isLoggedIn(request.getSession())) {
            final View view = new JspView("redirect:/index.jsp");
            return new ModelAndView(view).addObject("id", request.getAttribute("id"));
        }

        return new ModelAndView(new JspView("/login.jsp"));
    }
}
