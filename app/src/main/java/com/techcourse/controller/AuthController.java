package com.techcourse.controller;

import static nextstep.web.support.RequestMethod.GET;
import static nextstep.web.support.RequestMethod.POST;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @RequestMapping(value = "/login", method = POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        final Optional<User> optionalUser = InMemoryUserRepository.findByAccount(request.getParameter("account"));
        if (optionalUser.isEmpty()) {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
        final User user = optionalUser.get();
        log.info("User : {}", user);
        return login(request, user);
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }

    @RequestMapping(value = "/login", method = GET)
    public ModelAndView loginPage(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final Optional<User> optionalUser = UserSession.getUserFrom(req.getSession());
        if (optionalUser.isEmpty()) {
            return new ModelAndView(new JspView("/login.jsp"));
        }
        final User user = optionalUser.get();
        log.info("logged in {}", user.getAccount());
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/logout", method = GET)
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}
