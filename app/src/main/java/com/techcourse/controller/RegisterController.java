package com.techcourse.controller;

import static web.org.springframework.web.bind.annotation.RequestMethod.GET;
import static web.org.springframework.web.bind.annotation.RequestMethod.POST;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = GET)
    public String getRegisterPage(HttpServletRequest request, HttpServletResponse response) {
        return "/register.jsp";
    }

    @RequestMapping(value = "/register", method = POST)
    public String register(HttpServletRequest request, HttpServletResponse response) {
        User user = new User(2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email"));
        InMemoryUserRepository.save(user);
        return "redirect:/index.jsp";
    }
}
