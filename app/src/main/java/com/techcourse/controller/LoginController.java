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
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginView(final HttpServletRequest req, final HttpServletResponse res) {
        Optional<User> user = UserSession.getUserFrom(req.getSession());

        if (user.isEmpty()) {
            return new ModelAndView(new JspView("redirect:/login.jsp"));
        }

        log.info("logged in {}", user.get().getAccount());
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginUser(final HttpServletRequest req, final HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        Optional<User> user = InMemoryUserRepository.findByAccount(req.getParameter("account"));

        if (user.isEmpty()) {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }

        log.info("User: {}", user);
        return loginUser(req, user.get());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res) {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    private ModelAndView loginUser(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }
}
