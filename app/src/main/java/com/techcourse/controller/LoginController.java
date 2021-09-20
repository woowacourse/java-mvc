package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.domain.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.JspPage;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            View view = new JspView(JspPage.INDEX.value());
            return new ModelAndView(view);
        }
        return new ModelAndView(new JspView(JspPage.LOGIN.value()));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            View view = new JspView(JspPage.INDEX.redirect());
            return new ModelAndView(view);
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
            .map(user -> {
                LOGGER.info("User : {}", user);
                return new ModelAndView(loginCheck(req, user));
            })
            .orElse(new ModelAndView(
                new JspView(JspPage.UNAUTHORIZED.redirect())
            ));
    }

    private View loginCheck(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new JspView(JspPage.INDEX.redirect());
        } else {
            return new JspView(JspPage.UNAUTHORIZED.redirect());
        }
    }
}
