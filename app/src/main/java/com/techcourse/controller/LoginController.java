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
    private static final String PARAMETER = "account";

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView postLogin(final HttpServletRequest req, final HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        final var parameter = req.getParameter(PARAMETER);
        final var viewName = getViewName(req, parameter);
        return new ModelAndView(new JspView(viewName));
    }

    private String getViewName(final HttpServletRequest req, final String parameter) {
        final var nullableUser = InMemoryUserRepository.findByAccount(parameter);
        if (nullableUser.isEmpty()) {
            return "redirect:/401.jsp";
        }
        final var user = nullableUser.get();
        log.info("User : {}", user);
        return login(req, user);
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }
}
