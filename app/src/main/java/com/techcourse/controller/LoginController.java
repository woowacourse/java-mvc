package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LoginController {

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String INDEX_PAGE = "index.jsp";
    private static final String LOGIN_PAGE = "login.jsp";
    private static final String UNAUTHORIZED_PAGE = "401.jsp";
    private static final String SLASH = "/";


    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView readLoginPage(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(
                UserSession.getUserFrom(request.getSession())
                        .map(user -> {
                            log.info("logged in {}", user.getAccount());
                            return REDIRECT_PREFIX + SLASH + INDEX_PAGE;
                        })
                        .orElse(SLASH + LOGIN_PAGE)
        ));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_PREFIX + SLASH + INDEX_PAGE));
        }

        return new ModelAndView(new JspView(InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse(REDIRECT_PREFIX + SLASH + UNAUTHORIZED_PAGE)));
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return REDIRECT_PREFIX + SLASH + INDEX_PAGE;
        }
        return REDIRECT_PREFIX + SLASH + UNAUTHORIZED_PAGE;
    }
}
