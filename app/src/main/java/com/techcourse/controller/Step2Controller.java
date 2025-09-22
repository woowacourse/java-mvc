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

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView showForm(HttpServletRequest req, HttpServletResponse res) {
        final var modelAndView = new ModelAndView(new JspView("/form.jsp"));
        return modelAndView;
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public ModelAndView submitForm(HttpServletRequest req, HttpServletResponse res) {
        final String name = req.getParameter("name");
        final String email = req.getParameter("email");
        final String message = req.getParameter("message");

        final var modelAndView = new ModelAndView(new JspView("/result.jsp"));
        modelAndView.addObject("name", name);
        modelAndView.addObject("email", email);
        modelAndView.addObject("message", message);

        return modelAndView;
    }
}
