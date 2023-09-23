package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.view.JspView;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse ignored) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        final String viewName = InMemoryUserRepository.findByAccount(request.getParameter("account"))
                                                      .map(user -> {
                                                          log.info("User : {}", user);
                                                          return login(request, user);
                                                      })
                                                      .orElse("redirect:/401.jsp");

        return new ModelAndView(new JspView(viewName));
    }

    private String login(final HttpServletRequest requestuest, final User user) {
        if (user.checkPassword(requestuest.getParameter("password"))) {
            final HttpSession session = requestuest.getSession();

            session.setAttribute(UserSession.SESSION_KEY, user);

            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }
}
