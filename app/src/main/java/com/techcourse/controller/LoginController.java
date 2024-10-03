package com.techcourse.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView viewLoginPage(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return redirectToIndex();
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }

    private ModelAndView redirectToIndex() {
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return redirectToIndex();
        }
        return processLogin(request);
    }

    private ModelAndView processLogin(final HttpServletRequest request) {
        final Optional<User> user = InMemoryUserRepository.findByAccount(request.getParameter("account"));
        if (isInvalidUser(request, user)) {
            return new ModelAndView(new JspView("/401.jsp"));
        }

        final HttpSession session = request.getSession();
        session.setAttribute(UserSession.SESSION_KEY, user);
        return redirectToIndex();
    }

    private boolean isInvalidUser(final HttpServletRequest request, final Optional<User> user) {
        return user.isEmpty() || !(user.get().checkPassword(request.getParameter("password")));
    }
}
