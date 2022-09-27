package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showMain(final HttpServletRequest request, final HttpServletResponse response) {
        return ModelAndView.jspView("/index.jsp");
    }
}
