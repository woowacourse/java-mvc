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

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            log.info("User is logged in");
            return redirect("/index.jsp");
        }
        return redirect("/login.jsp");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            log.info("User is logged in");
            return redirect("/index.jsp");
        }

        final String account = request.getParameter("account");
        final String password = request.getParameter("password");

        return InMemoryUserRepository.findByAccount(account)
                .map(user -> {
                    log.info("User : {}", user);
                    return authenticate(request, user, password);
                })
                .orElseGet(() -> redirect("/401.jsp"));
    }

    private ModelAndView authenticate(final HttpServletRequest request, final User user, final String rawPassword) {
        if (user.checkPassword(rawPassword)) {
            request.getSession().setAttribute(UserSession.SESSION_KEY, user);
            log.info("User is logged in");
            return redirect("/index.jsp");
        }
        return redirect("/401.jsp");
    }

    private ModelAndView redirect(final String location) {
        return new ModelAndView(new JspView("redirect:" + location));
    }
}

