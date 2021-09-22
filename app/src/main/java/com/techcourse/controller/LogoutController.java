package com.techcourse.controller;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LogoutController {

    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Object logout(HttpServletRequest req, HttpServletResponse res) {
        final HttpSession session = req.getSession();

        Optional<User> user = UserSession.getUserFrom(session);
        user.ifPresent(value -> log.info("logged out {}", value.getAccount()));
        session.removeAttribute(UserSession.SESSION_KEY);

        return "redirect:/";
    }
}
