package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.exception.JspViewException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import static nextstep.mvc.view.ViewName.*;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView( REDIRECT_PREFIX + VIEW_INDEX));
        }
        User user = findUser(request);
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView( REDIRECT_PREFIX + VIEW_INDEX));
        } else {
            return new ModelAndView(new JspView( REDIRECT_PREFIX + VIEW_401));
        }
    }

    private User findUser(HttpServletRequest request) {
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .orElseThrow(JspViewException::new);
    }
}
