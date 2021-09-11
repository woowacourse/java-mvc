package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.UnauthorizedException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.handler.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        if (UserSession.isLoggedIn(session)) {
            return "redirect:/index.jsp";
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(session, req.getParameter("password"), user);
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
