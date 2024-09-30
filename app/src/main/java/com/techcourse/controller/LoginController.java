package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String REDIRECT_ROOT = "redirect:/";
    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String REDIRECT_LOGIN_JSP = "/login.jsp";
    private static final String REDIRECT_400_JSP = "redirect:/400.jsp";
    private static final String REDIRECT_401_JSP = "redirect:/401.jsp";
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        String viewName = UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return REDIRECT_INDEX_JSP;
                })
                .orElse(REDIRECT_LOGIN_JSP);
        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
        }

        if (req.getParameter(ACCOUNT) == null || req.getParameter(PASSWORD) == null) {
            return new ModelAndView(new JspView(REDIRECT_400_JSP));
        }

        String viewName = InMemoryUserRepository.findByAccount(req.getParameter(ACCOUNT))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(REDIRECT_401_JSP);
        return new ModelAndView(new JspView(viewName));
    }

    private String login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter(PASSWORD))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return REDIRECT_INDEX_JSP;
        }
        return REDIRECT_401_JSP;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView(REDIRECT_ROOT));
    }
}
