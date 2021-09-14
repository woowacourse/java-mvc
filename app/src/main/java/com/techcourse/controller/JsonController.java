package com.techcourse.controller;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class JsonController {

    @RequestMapping(value = "/api/mungto", method = RequestMethod.GET)
    public ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", new User(0L, "mungto", "password", "mungto@gmail.com"));
        return modelAndView;
    }
}
