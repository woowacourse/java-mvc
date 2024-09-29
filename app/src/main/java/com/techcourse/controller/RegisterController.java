package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public ModelAndView register(final HttpServletRequest req, final HttpServletResponse res) {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return ModelAndView.fromJsp("redirect:/index.jsp");
    }

    @RequestMapping(value = "/register/view", method = {RequestMethod.GET})
    public ModelAndView showRegisterView(final HttpServletRequest req, final HttpServletResponse res) {
        return ModelAndView.fromJsp("/register.jsp");
    }
}
