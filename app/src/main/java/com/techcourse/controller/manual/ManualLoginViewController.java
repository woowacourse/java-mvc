package com.techcourse.controller.manual;

import com.techcourse.controller.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualLoginViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(ManualLoginViewController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return "redirect:/index.jsp";
            })
            .orElse("/login.jsp");
    }
}
