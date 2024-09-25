package com.interface21.webmvc.servlet.samples.duplicated;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class DuplicateRequestController {

    public DuplicateRequestController() {
    }

    @RequestMapping(value = "/api/dup", method = RequestMethod.GET)
    public void test() {
    }

    @RequestMapping(value = "/api/dup", method = RequestMethod.GET)
    public void test2() {
    }
}
