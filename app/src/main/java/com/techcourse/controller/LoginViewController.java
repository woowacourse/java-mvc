package com.techcourse.controller;

import jakarta.servlet.http.HttpSession;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginViewController {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public String getLoginView(HttpSession session) {
        if (UserSession.isLoggedIn(session)) {
            return "redirect:/index.jsp";
        }

        return "/login.jsp";
    }
}
