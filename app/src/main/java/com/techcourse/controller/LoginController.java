package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.UnauthorizedException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkPassword(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (UserSession.isLoggedIn(session)) {
            return "redirect:/index.jsp";
        }
        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> checkPassword(session, req.getParameter("password"), user))
                .orElse("redirect:/401.jsp");
    }

    private String checkPassword(HttpSession session, String password, User user) {
        if (user.checkPassword(password)) {
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        throw new UnauthorizedException();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req) {
        final HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public String loginPage(HttpServletRequest req) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> "redirect:/index.jsp")
                .orElse("/login.jsp");
    }
}
