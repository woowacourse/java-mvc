package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private static final String JSP_EXTENSION = ".jsp";

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public ModelAndView findUserId(HttpServletRequest request) {
        log.info("test controller get method, url -> {}", request.getRequestURI());
        String viewNameWithoutExtension = request.getRequestURI();
        final ModelAndView modelAndView = new ModelAndView(
            new JspView(viewNameWithoutExtension + JSP_EXTENSION));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping(value = "/post-test", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request) {
        log.info("test controller get method, url -> {}", request.getRequestURI());
        String viewNameWithoutExtension = request.getRequestURI();
        final ModelAndView modelAndView = new ModelAndView(
            new JspView(viewNameWithoutExtension + JSP_EXTENSION));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }
}
