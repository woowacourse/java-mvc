package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HelloController {
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(value = "/api/hello", method = RequestMethod.GET)
    public ModelAndView hello(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: POST, Request URI: {}", request.getRequestURI());

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("Hello", "Hello, Hi!");
        return modelAndView;
    }
}
