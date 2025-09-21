package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.RedirectView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request) {
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
            .map(user -> {
                log.info("User : {}", user);
                return doLogin(request, user);
            })
            .orElse(new ModelAndView(new RedirectView("/401.jsp")));
    }

    private ModelAndView doLogin(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new RedirectView("/index.jsp"));
        }
        return new ModelAndView(new RedirectView("/401.jsp"));
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView view(final HttpServletRequest request) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new RedirectView("/index.jsp"));
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }
}
