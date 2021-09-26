package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/")
    public String index(HttpServletRequest req, HttpServletResponse res) {
        return "/index.jsp";
    }
}
