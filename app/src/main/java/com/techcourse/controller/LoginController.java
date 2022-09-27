package com.techcourse.controller;

import java.util.Optional;

import com.techcourse.LogUtil;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginView(final HttpServletRequest request, final HttpServletResponse response) {
        LogUtil.requestInfo("/login", RequestMethod.GET);
        return new ModelAndView(getLoginViewIfNotLoggedIn(request));
    }

    private View getLoginViewIfNotLoggedIn(final HttpServletRequest request) {
        Optional<User> user = UserSession.getUserFrom(request.getSession());
        if (user.isPresent()) {
            LogUtil.info("logged in {}", user.get().getAccount());
            return new JspView("redirect:/index.jsp");
        }
        return new JspView("/login.jsp");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {

        if (UserSession.isLoggedIn(request.getSession())) {
            LogUtil.info("User is logged in");
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        User user = InMemoryUserRepository.findByAccount(request.getParameter("account"))
            .orElseThrow(() -> new IllegalArgumentException("user not found"));
        LogUtil.info("User: {}", user);
        return new ModelAndView(login(request, user));
    }

    private View login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new JspView("redirect:/index.jsp");
        } else {
            return new JspView("redirect:/401.jsp");
        }
    }
}
