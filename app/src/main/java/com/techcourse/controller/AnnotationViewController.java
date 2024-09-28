package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AnnotationViewController {

    private static final Logger log = LoggerFactory.getLogger(AnnotationViewController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homeView(HttpServletRequest request, HttpServletResponse response) {
        return "/index.jsp";
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public String loginView(HttpServletRequest request, HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public String registerView(HttpServletRequest request, HttpServletResponse response) {
        return "/register.jsp";
    }
}
