package com.techcourse.controller;

import jakarta.servlet.http.HttpSession;
import nextstep.mvc.annotation.Controller;
import nextstep.mvc.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginViewController {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);
    private static final String REDIRECT_HOME = "redirect:/index.jsp";

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(HttpSession httpSession) {
        return UserSession.getUserFrom(httpSession)
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return REDIRECT_HOME;
            })
            .orElse("/login.jsp");
    }
}
