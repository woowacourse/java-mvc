package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;

public class LoginService {

    public String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        } else {
            return "redirect:/401.jsp";
        }
    }
}
