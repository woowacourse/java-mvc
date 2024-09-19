package com.interface21.webmvc.servlet.samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class DuplicateController {

    @RequestMapping(value = "/api/test", method = RequestMethod.GET)
    public void test1() {
    }

    @RequestMapping(value = "/api/test", method = RequestMethod.GET)
    public void test2() {
    }
}
