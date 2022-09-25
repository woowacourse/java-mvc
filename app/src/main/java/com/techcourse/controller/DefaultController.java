package com.techcourse.controller;

import com.techcourse.LogUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class DefaultController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView defaultView(final HttpServletRequest request, final HttpServletResponse response) {
        LogUtil.requestInfo("/", RequestMethod.POST);
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
