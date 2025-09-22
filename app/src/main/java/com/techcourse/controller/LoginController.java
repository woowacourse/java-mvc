package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.ModelAndView;
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

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView getLoginView(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        Optional<User> user = UserSession.getUserFrom(req.getSession());
        if (user.isPresent()) {
            log.info("logged in {}", user.get().getAccount());
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        if (req.getParameter("account") == null) {
            new ModelAndView(new JspView("redirect:/404.jsp"));
        }

        Optional<User> account = InMemoryUserRepository.findByAccount(req.getParameter("account"));
        if (account.isPresent()) {
            log.info("User : {}", account.get());
            return processLogin(req, account.get());
        }
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }

    private ModelAndView processLogin(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }
}
