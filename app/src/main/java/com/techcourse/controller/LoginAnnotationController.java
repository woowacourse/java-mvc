package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import air.annotation.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LoginAnnotationController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginView(HttpServletRequest request, HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
                          .map(user -> "redirect:/index.jsp")
                          .orElse("/login.jsp");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView("redirect:/index.jsp");
        }
        return InMemoryUserRepository.findByAccount("gugu")
                                     .map(user -> login(request, user))
                                     .orElse(new ModelAndView("redirect:/401.jsp"));
    }

    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword("password")) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView("redirect:/index.jsp");
        } else {
            return new ModelAndView("redirect:/401.jsp");
        }
    }
}
