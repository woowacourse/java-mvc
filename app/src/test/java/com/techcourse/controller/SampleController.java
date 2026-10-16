package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class SampleController {

    @RequestMapping(value = "/annotated", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/annotated.jsp"))
                .addObject("id", request.getParameter("id"));
    }

    @RequestMapping("/method-selection")
    public ModelAndView defaultMethod(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/default.jsp"));
    }

    @RequestMapping(value = "/method-selection", method = RequestMethod.GET)
    public ModelAndView specificMethod(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/specific.jsp"));
    }

    @RequestMapping(value = "/annotated-error", method = RequestMethod.GET)
    public ModelAndView fail(final HttpServletRequest request, final HttpServletResponse response) {
        throw new IllegalStateException("controller failure");
    }
}
