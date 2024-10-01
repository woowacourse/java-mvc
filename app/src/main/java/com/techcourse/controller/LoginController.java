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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (UserSession.isLoggedIn(session)) {
            return new ModelAndView(new JspView("redirect:/"));
        }
        return new ModelAndView(new JspView("login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);

        if (optionalUser.isEmpty() || !optionalUser.get().checkPassword(password)) {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
        HttpSession session = request.getSession();
        session.setAttribute(UserSession.SESSION_KEY, optionalUser.get());
        return new ModelAndView(new JspView("redirect:/"));
    }
}
