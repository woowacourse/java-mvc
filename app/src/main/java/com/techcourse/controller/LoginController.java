package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.UnAuthorizedException;
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

import java.util.Optional;

@Controller
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest request, HttpServletResponse response) {
        final Optional<User> sessionUser = UserSession.getUserFrom(request.getSession());
        if (sessionUser.isPresent()) {
            final User user = sessionUser.get();
            LOG.info("logged in {}", user.getAccount());
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        final String requestAccount = request.getParameter("account");
        final String requestPassword = request.getParameter("password");
        LOG.info("로그인 요청 => account : {}, password: {}", requestAccount, requestPassword);
        try {
            final User user = loginService.login(requestAccount, requestPassword);
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        } catch (UnAuthorizedException e) {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
    }
}
