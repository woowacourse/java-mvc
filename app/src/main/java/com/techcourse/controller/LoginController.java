package com.techcourse.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static final String REDIRECT_401 = "redirect:/401.jsp";
    private static final String REDIRECT_INDEX = "redirect:/index.jsp";
    private static final String LOGIN_VIEW_NAME = "/login.jsp";

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView(getViewName(req)));
    }

    private String getViewName(HttpServletRequest req) {
        return UserSession.getUserFrom(req.getSession())
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return REDIRECT_INDEX;
            })
            .orElse(LOGIN_VIEW_NAME);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView executeLogin(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_INDEX));
        }

        return new ModelAndView(new JspView(getAccount(req)));
    }

    private String getAccount(final HttpServletRequest req) {
        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
            .map(user -> {
                log.info("User : {}", user);
                return login(req, user);
            })
            .orElse(REDIRECT_401);
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return REDIRECT_INDEX;
        }
        return REDIRECT_401;
    }
}
