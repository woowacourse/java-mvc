package com.techcourse.controller;

import com.techcourse.domain.User;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import web.org.springframework.web.bind.annotation.GetMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

@Controller
public class ProfileController {

    @GetMapping("/me")
    public ModelAndView getProfile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        JsonView view = new JsonView();
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("account", user.getAccount());

        return modelAndView;
    }
}
