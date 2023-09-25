package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.GetMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

@Controller
public class UserController {

    @GetMapping("/api/user")
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        final String account = request.getParameter("account");

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow();

        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
