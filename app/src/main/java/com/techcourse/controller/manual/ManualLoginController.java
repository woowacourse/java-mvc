package com.techcourse.controller.manual;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualLoginController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(ManualLoginController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return "redirect:/index.jsp";
        }
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
            .map(user -> {
                log.info("User: {}", user);
                return login(request, user);
            })
            .orElse("redirect:/401.jsp");
    }

    private String login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);

            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }
}
