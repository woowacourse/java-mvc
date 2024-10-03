package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return redirectToIndex();
        }

        return InMemoryUserRepository.findByAccount(req.getParameter(ACCOUNT))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(redirectToUnauthorized());
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter(PASSWORD))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return redirectToIndex();
        }
        return redirectToUnauthorized();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginView(final HttpServletRequest req, final HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return redirectToIndex();
                })
                .orElse(redirectToLogin());
    }

    private ModelAndView redirectToIndex() {
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    private ModelAndView redirectToUnauthorized() {
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }

    private ModelAndView redirectToLogin() {
        return new ModelAndView(new JspView("/login.jsp"));
    }
}
