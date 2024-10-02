package com.techcourse.controller;

import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(final HttpServletRequest request, final Map<String, Object> model) {
        return findUserFromSession(request).map(user -> {
            log.info("logged in {}", user.getAccount());
            return "redirect:/index.jsp";
        }).orElseGet(() -> "/login.jsp");
    }

    private Optional<User> findUserFromSession(final HttpServletRequest request) {
        return UserSession.getUserFrom(request.getSession());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(final HttpServletRequest request, final Map<String, Object> model) {
        if (checkLogin(request)) {
            return "redirect:/index.jsp";
        }

        return findUserByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElseGet(() -> "redirect:/401.jsp");
    }

    private boolean checkLogin(final HttpServletRequest req) {
        return UserSession.isLoggedIn(req.getSession());
    }

    private Optional<User> findUserByAccount(final String account) {
        return InMemoryUserRepository.findByAccount(account);
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }
}
