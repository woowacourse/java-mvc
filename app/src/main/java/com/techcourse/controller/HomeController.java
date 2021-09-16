package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import static com.techcourse.view.ViewName.INDEX_JSP_VIEW_NAME;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView homeView(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(INDEX_JSP_VIEW_NAME));
    }
}
