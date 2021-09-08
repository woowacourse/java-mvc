package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import air.annotation.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LoginAnnotationController {

    @RequestMapping(value = "/login/v2", method = RequestMethod.POST)
    public ModelAndView loginV2(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return InMemoryUserRepository.findByAccount("gugu")
                                     .map(user -> login(request, user))
                                     .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword("password")) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        } else {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
    }
}
