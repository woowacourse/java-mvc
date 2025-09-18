package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
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

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView view(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final View view = UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return new JspView("redirect:/index.jsp");
                })
                .orElse(new JspView("/login.jsp"));

        return new ModelAndView(view);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginView(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            final View view = new JspView("redirect:/index.jsp");
            return new ModelAndView(view);
        }

        final View view = InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(new JspView("redirect:/401.jsp"));

        return new ModelAndView(view);
    }

    private View login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new JspView("redirect:/index.jsp");
        }
        return new JspView("redirect:/401.jsp");
    }
}
