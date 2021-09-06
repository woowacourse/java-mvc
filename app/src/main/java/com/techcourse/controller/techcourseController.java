package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class techcourseController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView base(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
