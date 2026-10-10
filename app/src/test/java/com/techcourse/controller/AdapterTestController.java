package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdapterTestController {

    @RequestMapping(value = "/adapter-test", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/adapter.jsp")).addObject("message", "hello");
    }

    @RequestMapping(value = "/adapter-redirect", method = RequestMethod.POST)
    public ModelAndView redirect(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/adapter-error", method = RequestMethod.GET)
    public ModelAndView fail(final HttpServletRequest request, final HttpServletResponse response) {
        throw new IllegalStateException("controller failure");
    }
}
