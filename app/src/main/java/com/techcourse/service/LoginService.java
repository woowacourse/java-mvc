package com.techcourse.service;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.function.Function;
import nextstep.web.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final InMemoryUserRepository userRepository;

    public LoginService(final InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String loginViewName(final HttpSession session) {
        return UserSession.getUserFrom(session)
                .map(this::toLoginSuccessUrl)
                .orElse("/login.jsp");
    }

    private String toLoginSuccessUrl(final User user) {
        log.info("logged in {}", user.getAccount());
        return "redirect:/index.jsp";
    }

    public String findByAccount(final HttpServletRequest req) {
        return userRepository.findByAccount(req.getParameter("account"))
                .map(convertUserToRedirectUrl(req))
                .orElse("redirect:/401.jsp");
    }

    private Function<User, String> convertUserToRedirectUrl(final HttpServletRequest req) {
        return user -> {
            log.info("User : {}", user);
            return login(req, user);
        };
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
