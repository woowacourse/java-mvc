package com.techcourse.controller;


import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView("index.jsp");
    }
}

