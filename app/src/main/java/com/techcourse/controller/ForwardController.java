package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;


@Controller
public class ForwardController {

    @RequestMapping(value = "/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
