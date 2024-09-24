package com.interface21.webmvc.servlet.samples.hided;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class PrivateConstructorController {

    private PrivateConstructorController() {
    }

    @RequestMapping(value = "/api/private", method = RequestMethod.GET)
    public void test() {
    }
}
