package com.techcourse.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView getLoginPage(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("login controller get method");
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("login controller post method");
        final String account = request.getParameter("account");
        final String password = request.getParameter("password");

        return InMemoryUserRepository.findByAccount(account)
            .map(user -> handleLogin(request, user, password))
            .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    private ModelAndView handleLogin(final HttpServletRequest request, final User user, final String password) {
        if (user.checkPassword(password)) {
            request.getSession().setAttribute(UserSession.SESSION_KEY, user);
            log.info("User logged in: {}", user);
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        log.info("Invalid login attempt for user: {}", user);
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }
}
