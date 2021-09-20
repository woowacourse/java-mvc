package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.JspPage;
import nextstep.web.support.RequestMethod;

@Controller
public class ForwardController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView(JspPage.INDEX.value()));
    }
}
