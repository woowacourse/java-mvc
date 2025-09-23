package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import java.util.UUID;

@Controller
public class JsonDemoController {

    @RequestMapping(value = "/demo/json", method = RequestMethod.GET)
    public ModelAndView get() {
        return new ModelAndView(new JsonView())
                .addObject("name", "dompoo")
                .addObject("age", 15)
                .addObject("uuid", UUID.randomUUID());
    }
}
