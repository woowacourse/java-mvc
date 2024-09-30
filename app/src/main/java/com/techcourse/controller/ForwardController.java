package com.techcourse.controller;

import com.interface21.web.bind.annotation.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ForwardController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView indexPage(final HttpServletRequest request, final HttpServletResponse response) {
        View view = new JspView("/index.jsp");

        return new ModelAndView(view);
    }
}
