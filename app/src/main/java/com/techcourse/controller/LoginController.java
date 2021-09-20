package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String show(HttpServletRequest request, HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return "redirect:/index.jsp";
            })
            .orElse("/login.jsp");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return "redirect:/index.jsp";
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
            .map(user -> {
                log.info("User : {}", user);
                return login(request, user);
            })
            .orElse("redirect:/401.jsp");
    }

    private String login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        } else {
            return "redirect:/401.jsp";
        }
    }
}
