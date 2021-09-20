package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;


@Controller
public class ForwardController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "/index.jsp";
    }
}
