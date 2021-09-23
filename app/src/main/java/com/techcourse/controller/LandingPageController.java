package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import static com.techcourse.controller.Page.INDEX_JSP;

@Controller
public class LandingPageController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView(INDEX_JSP.getPath()));
    }
}
