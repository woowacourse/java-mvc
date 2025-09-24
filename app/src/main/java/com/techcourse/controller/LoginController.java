package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.RedirectView;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new RedirectView("/index.jsp"));
        }
        Optional<User> user = InMemoryUserRepository.findByAccount(req.getParameter("account"));
        if (user.isPresent()) {
            log.info("User : {}", user);
            return login(req, user.get());
        }
        return new ModelAndView(new RedirectView("/401.jsp"));
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new RedirectView("/index.jsp"));
        }
        return new ModelAndView(new RedirectView("/401.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) {
        Optional<User> user = UserSession.getUserFrom(req.getSession());
        if (user.isPresent()) {
            log.info("logged in {}", user.get().getAccount());
            return new ModelAndView(new RedirectView("/index.jsp"));
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }
}
