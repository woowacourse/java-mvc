package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class WelcomeViewController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String execute(final HttpServletRequest request, final HttpServletResponse response) {
        return "index.jsp";
    }
}
