package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

@Controller
@RequestMapping
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHome() {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
