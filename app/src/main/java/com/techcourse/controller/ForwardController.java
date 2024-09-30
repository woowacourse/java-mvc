package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

@Controller
public class ForwardController {

    public static final String INDEX_JSP = "/index.jsp";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView loadIndex(HttpServletRequest req, HttpServletResponse res) {
        return stringToModelAndView(INDEX_JSP);
    }

    private ModelAndView stringToModelAndView(String viewName) {
        return new ModelAndView(new JspView(viewName));
    }
}
