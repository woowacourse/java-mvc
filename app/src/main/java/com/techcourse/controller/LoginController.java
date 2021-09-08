package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.UnauthorizedException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.handler.asis.Controller;
import nextstep.web.annotation.RequestParam;
import nextstep.web.annotation.SessionAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@nextstep.web.annotation.Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public String execute(@RequestParam("account") String account, @RequestParam("password") String password,
            @SessionAttribute HttpSession session) throws Exception {
        if (UserSession.isLoggedIn(session)) {
            return "redirect:/index.jsp";
        }

        return InMemoryUserRepository.findByAccount(account)
                .map(user -> {
                    log.info("User : {}", user);
                    return login(session, password, user);
                })
                .orElse("redirect:/401.jsp");
    }

    private String login(HttpSession session, String password, User user) {
        if (user.checkPassword(password)) {
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        throw new UnauthorizedException();
    }
}
