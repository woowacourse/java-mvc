package com.techcourse.controller.annotation;

import static nextstep.web.support.RequestMethod.GET;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    private static final String HOME_PATH = "/index";

    @RequestMapping(value = "/", method = GET)
    public ModelAndView getHome(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Welcome to Home!");

        return new ModelAndView(new JspView(HOME_PATH));
    }
}
