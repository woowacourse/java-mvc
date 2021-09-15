package com.techcourse.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class AnnotatedController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
