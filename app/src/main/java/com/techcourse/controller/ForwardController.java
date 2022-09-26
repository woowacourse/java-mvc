package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class ForwardController {

    private static final String INDEX_JSP_VIEW_NAME = "index.jsp";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getMainView(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(INDEX_JSP_VIEW_NAME));
    }
}
