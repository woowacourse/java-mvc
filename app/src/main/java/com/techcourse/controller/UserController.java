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
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String REDIRECT_MAIN_PAGE = "redirect:/index.jsp";

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return REDIRECT_MAIN_PAGE;
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElseGet(() -> "redirect:/401.jsp");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        final HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginView(HttpServletRequest req, HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return REDIRECT_MAIN_PAGE;
                })
                .orElseGet(() -> "/login.jsp");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest req, HttpServletResponse res) {
        final User user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);
        return REDIRECT_MAIN_PAGE;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerView(HttpServletRequest req, HttpServletResponse res) {
        return "/register.jsp";
    }

    private String login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return REDIRECT_MAIN_PAGE;
        }
        return "redirect:/401.jsp";
    }
}
