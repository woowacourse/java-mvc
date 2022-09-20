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
public class AnnotationController {

    private static final Logger log = LoggerFactory.getLogger(AnnotationController.class);

    @RequestMapping(value = "/annotation", method = RequestMethod.GET)
    public ModelAndView testConnection(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("annotation controller get method");
        return null;
    }
}
