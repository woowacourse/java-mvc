package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class Step2Controller {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView showForm(HttpServletRequest req, HttpServletResponse res) {
        final var modelAndView = new ModelAndView(new JspView("/index.jsp"));
        return modelAndView;
    }
}
