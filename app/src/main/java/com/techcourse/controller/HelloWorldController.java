package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class HelloWorldController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("name", "dompoo");
        modelAndView.addObject("age", 15);
        modelAndView.addObject("uuid", UUID.randomUUID());
        return modelAndView;
    }
}
