package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspViewRenderer;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController implements JspViewRenderer {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginView(final HttpServletRequest request, final HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return redirect("/index.jsp");
                })
                .orElse(showPage("/login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginUser(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return redirect("/index.jsp");
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse(redirect("/401.jsp"));
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return redirect("/index.jsp");
        }
        return redirect("/401.jsp");
    }
}
