package com.techcourse.controller;


import com.techcourse.support.web.filter.ResourceFilter;
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
public class DummyController {
    private static final Logger log = LoggerFactory.getLogger(ResourceFilter.class);

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView base(HttpServletRequest request, HttpServletResponse response) {
        log.info("path : /test");
        final ModelAndView modelAndView = new ModelAndView(new JspView("/test.jsp"));
        modelAndView.addObject("unknown", "test");

        return modelAndView;
    }

}
