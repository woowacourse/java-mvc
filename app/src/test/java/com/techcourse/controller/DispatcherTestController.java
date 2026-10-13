package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DispatcherTestController {

    @RequestMapping(value = "/annotation-test", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/annotation.jsp"))
                .addObject("id", request.getParameter("id"));
    }

    @RequestMapping(value = "/annotation-test", method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("redirect:/annotation-test"));
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/annotation-login.jsp"));
    }

    @RequestMapping(value = "/annotation-error", method = RequestMethod.GET)
    public ModelAndView fail(final HttpServletRequest request, final HttpServletResponse response) {
        throw new IllegalStateException("요청 처리 실패");
    }
}
