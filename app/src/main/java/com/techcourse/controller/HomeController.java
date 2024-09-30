package com.techcourse.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexPage(final HttpServletRequest request, final Map<String, Object> model) {
        return "/index.jsp";
    }
}
