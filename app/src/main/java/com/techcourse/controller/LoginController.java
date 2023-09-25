package com.techcourse.controller;

import static web.org.springframework.web.bind.annotation.RequestMethod.GET;
import static web.org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";

    @RequestMapping(value = "/login", method = GET)
    public ModelAndView showLoginPage(final HttpServletRequest request, final HttpServletResponse response) {
        final String appropriatePage = findAppropriatePage(request);
        return new ModelAndView(new JspView(appropriatePage));
    }

    private String findAppropriatePage(final HttpServletRequest request) {
        return UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return REDIRECT_INDEX_JSP;
                })
                .orElse("/login.jsp");
    }

    @RequestMapping(value = "/login", method = POST)
    public ModelAndView doLogin(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String appropriatePage = findAppropriateByAuthorization(request);
        return new ModelAndView(new JspView(appropriatePage));

    }

    private String findAppropriateByAuthorization(final HttpServletRequest request) {
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

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }
}
