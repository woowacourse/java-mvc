package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.RedirectView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new RedirectView("/index.jsp"));
        }

        final String account = request.getParameter("account");
        if (account == null || account.isBlank()) {
            return new ModelAndView(new RedirectView("/login.jsp"));
        }

        return InMemoryUserRepository.findByAccount(account)
                .map(user -> {
                    log.info("logged in user : {}", user);
                    return login(request, user);
                })
                .orElse(new ModelAndView(new RedirectView("/login.jsp")));
    }
    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);

            return new ModelAndView(new RedirectView("/index.jsp"));
        }

        return new ModelAndView(new RedirectView("/login.jsp"));
    }
}
