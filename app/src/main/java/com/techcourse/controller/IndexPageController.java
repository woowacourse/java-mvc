package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.type.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class IndexPageController {

    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView getIndexPage(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
