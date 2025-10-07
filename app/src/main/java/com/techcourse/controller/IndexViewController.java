package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class IndexViewController {

    private static final Logger log = LoggerFactory.getLogger(IndexViewController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView indexView(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String viewName = "index.jsp";
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}
