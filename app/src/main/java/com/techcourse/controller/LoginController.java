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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return redirectToIndex();
        }

        return new ModelAndView(new JspView("/login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return redirectToIndex();
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> authenticate(request, user))
                .orElseGet(LoginController::redirectToUnauthorized);
    }

    private ModelAndView authenticate(final HttpServletRequest request, final User user) {
        log.info("User : {}", user);
        if (!user.checkPassword(request.getParameter("password"))) {
            return redirectToUnauthorized();
        }

        request.getSession().setAttribute(UserSession.SESSION_KEY, user);
        return redirectToIndex();
    }

    private static ModelAndView redirectToIndex() {
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    private static ModelAndView redirectToUnauthorized() {
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }
}
