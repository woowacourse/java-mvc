package com.techcourse.air.controller;

import com.techcourse.air.core.annotation.Controller;
import com.techcourse.air.domain.User;
import com.techcourse.air.mvc.core.view.ModelAndView;
import com.techcourse.air.mvc.web.annotation.RequestMapping;
import com.techcourse.air.mvc.web.support.RequestMethod;
import com.techcourse.air.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

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
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                                     .map(user -> login(request, user))
                                     .orElse(new ModelAndView("redirect:/401.jsp"));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        final HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }

    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView("redirect:/index.jsp");
        } else {
            return new ModelAndView("redirect:/401.jsp");
        }
    }
}
