package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class HomeController {

    private static final String INDEX_JSP = "index.jsp";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        return INDEX_JSP;
    }

}
