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

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    public static final String INDEX_JSP = "redirect:/index.jsp";
    public static final String REDIRECT_401_JSP = "redirect:/401.jsp";

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView showLoginPage(HttpServletRequest req, HttpServletResponse res) {

        String viewName = UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return INDEX_JSP;
                })
                .orElse("/login.jsp");

        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse res) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(INDEX_JSP));
        }

        String viewName = InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return validatePassword(request, user);
                })
                .orElse(REDIRECT_401_JSP);

        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }

    private String validatePassword(final HttpServletRequest request, final User user) {
        String password = request.getParameter("password");
        if (user.checkPassword(password)) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return INDEX_JSP;
        }
        return REDIRECT_401_JSP;
    }

}
