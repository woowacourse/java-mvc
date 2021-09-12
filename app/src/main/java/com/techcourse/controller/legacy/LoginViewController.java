package com.techcourse.controller.legacy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
    }
}
