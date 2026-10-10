package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class IntegrationController {

    @RequestMapping(value = "/integration", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/integration.jsp")).addObject("message", "hello");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView overrideLegacy(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("redirect:/integration"));
    }
}
