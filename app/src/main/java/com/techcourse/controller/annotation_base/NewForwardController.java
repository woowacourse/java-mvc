package com.techcourse.controller.annotation_base;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class NewForwardController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView indexPage(HttpServletRequest req, HttpServletResponse res) {
        return ModelAndView.fromJspView("/index.jsp");
    }
}
