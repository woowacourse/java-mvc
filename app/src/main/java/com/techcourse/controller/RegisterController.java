package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.RedirectView;

@Controller
public class RegisterController {

    @RequestMapping("/register")
    public ModelAndView register(final HttpServletRequest request, final HttpServletResponse response) {
        final var user = new User(2,
            request.getParameter("account"),
            request.getParameter("password"),
            request.getParameter("email"));
        InMemoryUserRepository.save(user);

        return new ModelAndView(new RedirectView("/index.jsp"));
    }
}
