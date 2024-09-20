package com.interface21.webmvc;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testGetMethod() {
        return "GET Response";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String testPostMethod() {
        return "POST Response";
    }
}
