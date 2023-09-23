package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/login")
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new RedirectView("/index.jsp"));
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
            .map(user -> {
                log.info("User : {}", user);
                return login(request, user);
            })
            .orElse(new ModelAndView(new RedirectView("/401.jsp")));
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
