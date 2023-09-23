package com.techcourse.controller.annotation;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            return "redirect:/index.jsp";
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return processLogin(req, user);
                })
                .orElse("redirect:/401.jsp");
    }

    private String processLogin(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String view(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return UserSession.getUserFrom(req.getSession())
                          .map(user -> {
                              log.info("logged in {}", user.getAccount());
                              return "redirect:/index.jsp";
                          })
                          .orElse("/login.jsp");
    }
}
