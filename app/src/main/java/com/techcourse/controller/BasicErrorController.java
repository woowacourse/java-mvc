package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class BasicErrorController {

    @RequestMapping(value = "/401", method = RequestMethod.GET)
    public ModelAndView findUnauthorizedView(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("/401");
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public ModelAndView findNotFoundView(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("/404");
    }

    @RequestMapping(value = "/500", method = RequestMethod.GET)
    public ModelAndView findInternalServerErrorView(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("/500");
    }

}
