package com.techcourse.support;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;

@Controller
public class AnnotatedController {

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public ModelAndView get() {
        return null;
    }
}
