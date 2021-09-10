package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nextstep.core.annotation.Autowired;
import nextstep.mvc.annotation.Controller;
import nextstep.mvc.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String REDIRECT_HOME = "redirect:/index.jsp";
    private static final String REDIRECT_UNAUTHORIZED = "redirect:/401.jsp";

    private final InMemoryUserRepository userRepository;

    @Autowired
    public LoginController(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(HttpSession httpSession) {
        return UserSession.getUserFrom(httpSession)
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return REDIRECT_HOME;
            })
            .orElse("/login.jsp");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest req) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return REDIRECT_HOME;
        }

        return userRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(REDIRECT_UNAUTHORIZED);
    }

    private String login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return REDIRECT_HOME;
        } else {
            return REDIRECT_UNAUTHORIZED;
        }
    }
}
