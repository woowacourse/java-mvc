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
public class SampleAnnotationController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/sample", method = RequestMethod.GET)
    public ModelAndView doSample(HttpServletRequest request, HttpServletResponse response) {
        log.info("sample controller get sample method invoked");
        final ModelAndView modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

}
