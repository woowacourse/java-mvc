package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final View REDIRECT_UNAUTHORIZED_VIEW = new JspView("redirect:/401.jsp");
    private static final JspView REDIRECT_INDEX_PAGE_VIEW = new JspView("redirect:/index.jsp");

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(REDIRECT_INDEX_PAGE_VIEW);
        }
        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
            .map(user -> checkUser(req, user))
            .orElse(new ModelAndView(REDIRECT_UNAUTHORIZED_VIEW));
    }

    private ModelAndView checkUser(final HttpServletRequest request, final User user) {
        log.info("User : {}", user);

        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(REDIRECT_INDEX_PAGE_VIEW);
        }
        return new ModelAndView(REDIRECT_UNAUTHORIZED_VIEW);
    }
}
