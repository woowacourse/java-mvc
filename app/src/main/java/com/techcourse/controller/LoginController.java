package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return redirectTo("/");
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User account : {}", user.getAccount());
                    return authenticate(request, user);
                })
                .orElseGet(() -> redirectTo("/401.jsp"));
    }

    private ModelAndView authenticate(HttpServletRequest request, User user) {
        if (!user.checkPassword(request.getParameter("password"))) {
            return redirectTo("/401.jsp");
        }

        HttpSession session = request.getSession();
        session.setAttribute(UserSession.SESSION_KEY, user);
        return redirectTo("/");
    }

    private ModelAndView redirectTo(String path) {
        return new ModelAndView(new JspView("redirect:" + path));
    }
}
