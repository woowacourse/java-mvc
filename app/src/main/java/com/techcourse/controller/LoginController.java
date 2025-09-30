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
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return redirect("/index.jsp");
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return authenticate(request, user);
                })
                .orElseGet(() -> redirect("/401.jsp"));
    }

    private ModelAndView authenticate(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            request.getSession().setAttribute(UserSession.SESSION_KEY, user);
            return redirect("/index.jsp");
        }
        return redirect("/401.jsp");
    }

    private ModelAndView redirect(final String path) {
        return new ModelAndView(new RedirectView(path));
    }
}
