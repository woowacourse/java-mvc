package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.AuthException;
import com.techcourse.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response){
        log.info("Method: POST, Request URI: {}", request.getRequestURI());

        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        final String account = request.getParameter("account");
        final String password = request.getParameter("password");

        try {
            User user = loginService.login(account, password);

            HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);

            return new ModelAndView(new JspView("redirect:/index.jsp"));
        } catch (AuthException e) {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: GET, Request URI: {}", request.getRequestURI());

        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("login.jsp"));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: GET, Request URI: {}", request.getRequestURI());

        final HttpSession session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}
