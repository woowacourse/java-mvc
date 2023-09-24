package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView executePostRegister(final HttpServletRequest req, final HttpServletResponse res) {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        final String viewName = "redirect:/index.jsp";

        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView executeGetRegisterView(final HttpServletRequest req, final HttpServletResponse res) {
        final String viewName = "/register.jsp";

        return new ModelAndView(new JspView(viewName));
    }
}
