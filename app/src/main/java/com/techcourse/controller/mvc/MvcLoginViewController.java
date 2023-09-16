package com.techcourse.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class MvcLoginViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(MvcLoginViewController.class);

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return MvcUserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
    }
}
