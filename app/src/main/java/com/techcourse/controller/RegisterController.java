package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String viewRegister(final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        return "/register.jsp";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String handleRegister(final HttpServletRequest request,
                                 final HttpServletResponse response) throws Exception {
        final var user = new User(2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email"));
        InMemoryUserRepository.save(user);

        return "redirect:/";
    }
}
