package com.techcourse.controller;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class WelcomeController {

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView welcome() {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
