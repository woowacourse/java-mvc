package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;

import static com.interface21.web.bind.annotation.RequestMethod.GET;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = GET)
    public String view() {
        return "/index.jsp";
    }
}
