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
public class MultiInputController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiInputController.class);

    @RequestMapping(value = "/api/multi", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        String input1 = request.getParameter("input1");
        String input2 = request.getParameter("input2");

        LOGGER.debug("input1 : {}", input1);
        LOGGER.debug("input2 : {}", input2);

        ModelAndView modelAndView = new ModelAndView(new JsonView());

        modelAndView.addObject("input1", input1);
        modelAndView.addObject("input2", input2);
        return modelAndView;
    }
}
