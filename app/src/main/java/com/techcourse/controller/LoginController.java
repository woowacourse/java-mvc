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

@Controller(path = "/login")
public class LoginController {

    private static final String REDIRECT_INDEX = "redirect:/index.jsp";
    private static final String REDIRECT_401 = "redirect:/401.jsp";
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView doLogin(final HttpServletRequest req, final HttpServletResponse res) {
        final String view = findViewName(req);
        final var modelAndView = new ModelAndView(new JspView(view));
        modelAndView.addObject("id", req.getAttribute("id"));

        return modelAndView;
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) {
        final String loginView = UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return REDIRECT_INDEX;
                })
                .orElse("/login.jsp");
        final var modelAndView = new ModelAndView(new JspView(loginView));
        modelAndView.addObject("id", req.getAttribute("id"));

        return modelAndView;
    }

    public String findViewName(final HttpServletRequest req) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return REDIRECT_INDEX;
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(REDIRECT_401);
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return REDIRECT_INDEX;
        }
        return REDIRECT_401;
    }
}
