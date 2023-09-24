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
import webmvc.org.springframework.web.servlet.view.RedirectView;

@Controller(path = "/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showLoginPage(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return UserSession.getUserFrom(req.getSession())
                          .map(user -> {
                              log.info("logged in {}", user.getAccount());
                              return new ModelAndView(new RedirectView("/index.jsp"));
                          })
                          .orElse(new ModelAndView(new RedirectView("/login.jsp")));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new RedirectView("/index.jsp"));
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                                     .map(user -> {
                                         log.info("User : {}", user);
                                         return login(req, user);
                                     })
                                     .orElse(new ModelAndView(new RedirectView("/404.jsp")));
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new RedirectView("/index.jsp"));
        }
        return new ModelAndView(new RedirectView("/401.jsp"));
    }
}
