package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @RequestMapping(value = "/userinfo", method = RequestMethod.GET)
    public ModelAndView getUser(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var modelAndView = new ModelAndView(new JsonView());

        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    modelAndView.addObject("user", user);
                    return modelAndView;
                })

                .orElseGet(() -> {
                    modelAndView.addObject("error", "User not found");
                    return modelAndView;
                });
    }
}
