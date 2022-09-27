package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse res) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        return new ModelAndView(new JspView(newLoginAndGetViewName(request)));
    }

    private String newLoginAndGetViewName(final HttpServletRequest request) {
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse("redirect:/401.jsp");
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        } else {
            return "redirect:/401.jsp";
        }
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView loginView(final HttpServletRequest request, final HttpServletResponse res) {
        return new ModelAndView(new JspView(loginCheckAndGetViewName(request)));
    }

    private String loginCheckAndGetViewName(final HttpServletRequest req) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(final HttpServletRequest request, final HttpServletResponse res) {
        final var session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}
