package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AnnotationController {

    private static final Logger log = LoggerFactory.getLogger(AnnotationController.class);

    @RequestMapping(value = "/annotation", method = RequestMethod.GET)
    public ModelAndView getByAnnotation(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("annotation handler get method");
        return new ModelAndView(new JspView("index.jsp"));
    }

    @RequestMapping(value = "/annotation-redirect", method = RequestMethod.GET)
    public ModelAndView redirectByAnnotation(HttpServletRequest request, HttpServletResponse response) {
        log.info("annotation-redirect handler get method");
        return new ModelAndView(new JspView("redirect:401.jsp"));
    }
}
