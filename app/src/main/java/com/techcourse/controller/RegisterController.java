package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.dto.RegisterRequest;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import web.org.springframework.web.bind.annotation.RequestBody;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(@RequestBody final RegisterRequest registerRequest) {
        final var user = new User(2,
                registerRequest.getAccount(),
                registerRequest.getPassword(),
                registerRequest.getEmail());
        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView show() {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
