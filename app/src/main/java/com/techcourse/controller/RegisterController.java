package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(execute(req, res));
    }

    private View execute(HttpServletRequest req, HttpServletResponse res) {
        User user = new User(2,
            req.getParameter("account"),
            req.getParameter("password"),
            req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return new JspView("redirect:/index.jsp");
    }
}
