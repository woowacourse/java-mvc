package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class FunController {

    @RequestMapping(value = "/fun", method = RequestMethod.GET)
    public ModelAndView base(HttpServletRequest request, HttpServletResponse response) {
        final ModelAndView modelAndView = new ModelAndView(new JspView("/fun.jsp"));
        modelAndView.addObject("greetings", "안녕하세요 🙂");

        return modelAndView;
    }
}
