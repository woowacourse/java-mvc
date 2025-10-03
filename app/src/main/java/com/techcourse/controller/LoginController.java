package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.ModelAndView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(JspView.redirectTo("/index.jsp"));
        }
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> login(request, user))
                .orElse(new ModelAndView(new JspView("/401.jsp")));
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse response) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> new ModelAndView(JspView.redirectTo("/index.jsp")))
                .orElse(new ModelAndView(new JspView("/login.jsp")));
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.isSamePassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(JspView.redirectTo("/index.jsp"));
        }
        return new ModelAndView(new JspView("/401.jsp"));
    }
}
