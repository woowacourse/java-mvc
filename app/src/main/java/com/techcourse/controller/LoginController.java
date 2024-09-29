package com.techcourse.controller;

import java.util.Optional;

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

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(final HttpServletRequest request, final HttpServletResponse response) {
        return findUserFromSession(request).map(user -> {
            log.info("logged in {}", user.getAccount());
            return convertModelAndView("redirect:/index.jsp");
        }).orElseGet(() -> convertModelAndView("/login.jsp"));
    }

    private Optional<User> findUserFromSession(final HttpServletRequest request) {
        return UserSession.getUserFrom(request.getSession());
    }

    private ModelAndView convertModelAndView(final String pageUri) {
        final JspView view = new JspView(pageUri);
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (checkLogin(request)) {
            return convertModelAndView("redirect:/index.jsp");
        }

        return findUserByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    final String redirectPageUri = login(request, user);
                    return convertModelAndView(redirectPageUri);
                })
                .orElseGet(() -> convertModelAndView("redirect:/401.jsp"));
    }

    private boolean checkLogin(final HttpServletRequest req) {
        return UserSession.isLoggedIn(req.getSession());
    }

    private Optional<User> findUserByAccount(final String account) {
        return InMemoryUserRepository.findByAccount(account);
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
