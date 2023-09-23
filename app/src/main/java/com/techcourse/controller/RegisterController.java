package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.supports.adapter.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.view.JspView;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(final HttpServletRequest request, final HttpServletResponse ignored) {
        final User user = new User(
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email")
        );

        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }
}
