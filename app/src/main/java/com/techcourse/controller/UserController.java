package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

@Controller
public class UserController {

    @RequestMapping(value = "/api/user/me", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());

        UserSession.getUserFrom(req.getSession())
                .ifPresentOrElse(
                        user -> modelAndView.addObject("user", user),
                        () -> modelAndView.addObject("user", null)
                );

        return modelAndView;
    }
}
