package com.techcourse.controller;

import static com.techcourse.controller.JspConstants.LOGIN_JSP;
import static com.techcourse.controller.JspConstants.REDIRECT_401_JSP;
import static com.techcourse.controller.JspConstants.REDIRECT_INDEX_JSP;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView renderLoginView(final HttpServletRequest req, final HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
                })
                .orElseGet(() -> new ModelAndView(LOGIN_JSP));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(REDIRECT_INDEX_JSP);
        }

        final Optional<User> user = findLoginUser(req);
        if (findLoginUser(req).isPresent()) {
            final var session = req.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(REDIRECT_INDEX_JSP);
        }
        return new ModelAndView(REDIRECT_401_JSP);
    }

    private Optional<User> findLoginUser(final HttpServletRequest req) {
        final Optional<User> user = InMemoryUserRepository.findByAccount(req.getParameter(ACCOUNT));
        if (user.isPresent() && user.get().checkPassword(req.getParameter(PASSWORD))) {
            return user;
        }
        return Optional.empty();
    }
}
