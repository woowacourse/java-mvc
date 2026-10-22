package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ModelTestController {

    @RequestMapping(value = "/model-test", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/model.jsp")).addObject("id", request.getParameter("id"));
    }

    @RequestMapping(value = "/failing-test", method = RequestMethod.GET)
    public ModelAndView fail(final HttpServletRequest request, final HttpServletResponse response) {
        throw new IllegalStateException("컨트롤러 실행 실패");
    }
}
