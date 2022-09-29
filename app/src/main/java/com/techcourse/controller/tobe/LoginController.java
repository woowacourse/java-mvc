package com.techcourse.controller.tobe;

import static com.techcourse.controller.UserSession.SESSION_KEY;
import static com.techcourse.controller.UserSession.getUserFrom;
import static com.techcourse.controller.UserSession.isLoggedIn;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(com.techcourse.controller.asis.LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (isLoggedIn(req.getSession())) {
            return "redirect:/index.jsp";
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> checkLogin(req, user))
                .orElse("redirect:/401.jsp");
    }

    private String checkLogin(final HttpServletRequest request, final User user) {
        log.info("User : {}", user);

        if (!user.checkPassword(request.getParameter("password"))) {
            return "redirect:/401.jsp";
        }

        request.setAttribute(SESSION_KEY, user);
        return "/index";
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public String showLogin(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final Optional<User> user = getUserFrom(req.getSession());

        if (user.isPresent()) {
            return "redirect:/index.jsp";
        }
        return "/login";
    }
}
