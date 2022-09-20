package com.techcourse.controller.v2;

import com.techcourse.controller.UserSession;
import com.techcourse.controller.v1.LoginViewController;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginV2Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginV2Controller.class);

    /**
     *         controllers.put("/login/view", new LoginViewController());
     *         controllers.put("/logout", new LogoutController());
     */
    @RequestMapping(value = "/v2/login/view")
    public String loginView(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
    }

    @RequestMapping(value = "/v2/login")
    public String login(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            return "redirect:/index.jsp";
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse("redirect:/401.jsp");
    }

    @RequestMapping(value = "/v2/logout")
    public String logout(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        } else {
            return "redirect:/401.jsp";
        }
    }
}
