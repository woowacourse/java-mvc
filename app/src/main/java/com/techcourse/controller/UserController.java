package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");

        log.debug("user id : {}", account);

        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
        if (optionalUser.isEmpty()) {
            return new ModelAndView(new JspView("/404.jsp"));
        }

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = optionalUser.get();
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
