package com.techcourse.controller.v1;

import com.techcourse.controller.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handler.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginViewOldController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginViewOldController.class);

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        log.info("LoginViewV1Controller.execute");
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
    }
}
