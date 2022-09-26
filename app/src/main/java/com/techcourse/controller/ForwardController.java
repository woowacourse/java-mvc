package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ForwardController {

    private static final Logger log = LoggerFactory.getLogger(ForwardController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView forward(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("request {} {}", request.getMethod(), request.getRequestURI());
        return new ModelAndView("/index.jsp");
    }
}
