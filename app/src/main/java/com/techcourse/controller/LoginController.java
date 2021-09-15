package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.AuthException;
import com.techcourse.service.LoginService;
import com.techcourse.service.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.ViewName;
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
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: POST, Request URI: {}", request.getRequestURI());

        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(ViewName.REDIRECT_INDEX));
        }

        try {
            User user = loginService.login(LoginDto.of(request));

            HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);

            return new ModelAndView(new JspView(ViewName.REDIRECT_INDEX));
        } catch (AuthException e) {
            return new ModelAndView(new JspView(ViewName.REDIRECT_UNAUTHORIZED));
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: GET, Request URI: {}", request.getRequestURI());

        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(ViewName.REDIRECT_INDEX));
        }
        return new ModelAndView(new JspView(ViewName.LOGIN));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: GET, Request URI: {}", request.getRequestURI());

        final HttpSession session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView(ViewName.REDIRECT_HOME));
    }
}
