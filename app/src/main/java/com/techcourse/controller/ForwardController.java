package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ForwardController {

    private static final String PATH = "/index.jsp";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView(PATH));
    }
}
