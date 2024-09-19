package com.interface21.webmvc.servlet.util;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test2() {
    }
}
