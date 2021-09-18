package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class HomeController {

    private static final String HOME_VIEW = "index.jsp";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpServletRequest request, HttpServletResponse response) {
        return HOME_VIEW;
    }
}
