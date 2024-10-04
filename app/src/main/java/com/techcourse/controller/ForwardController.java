package com.techcourse.controller;

import static com.interface21.web.bind.annotation.RequestMethod.GET;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ForwardController {

    @RequestMapping(value = "/", method = GET)
    public ModelAndView home(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
