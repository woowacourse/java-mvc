package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AnnotatedRegisterController {

    @RequestMapping(value = "/register2", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req) {
        JspView jspView = new JspView("register");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/register2", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req) {
        JspView jspView = new JspView("register");
        return new ModelAndView(jspView);
    }
}
