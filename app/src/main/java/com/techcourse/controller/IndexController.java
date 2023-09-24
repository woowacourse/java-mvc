package com.techcourse.controller;

import static com.techcourse.controller.JspConstants.INDEX_JSP;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView renderIndexView(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(INDEX_JSP);
    }
}
