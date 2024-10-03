package com.techcourse.controller;

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
    private static final String LOGIN_SUCCESS_REDIRECT = "redirect:/index.jsp";
    private static final String LOGIN_FAILED_REDIRECT = "redirect:/401.jsp";
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    public static final String LOGIN_PAGE = "/login.jsp";
    public static final String LOGOUT_REDIRECT = "redirect:/";

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        String viewName = UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return LOGIN_SUCCESS_REDIRECT;
                })
                .orElse(LOGIN_PAGE);
        JspView view = new JspView(viewName);
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView processLogin(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            JspView view = new JspView(LOGIN_SUCCESS_REDIRECT);
            return new ModelAndView(view);
        }

        String viewName = InMemoryUserRepository.findByAccount(req.getParameter(ACCOUNT))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(LOGIN_FAILED_REDIRECT);

        JspView view = new JspView(viewName);
        return new ModelAndView(view);
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter(PASSWORD))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return LOGIN_SUCCESS_REDIRECT;
        }
        return LOGIN_FAILED_REDIRECT;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res) {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        JspView jspView = new JspView(LOGOUT_REDIRECT);
        return new ModelAndView(jspView);
    }
}
