package com.techcourse.controller.typed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techcourse.controller.UserSession;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;

public class TypedLoginViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(TypedLoginViewController.class);

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return "redirect:/index.jsp";
            })
            .orElse("/login.jsp");
    }
}
