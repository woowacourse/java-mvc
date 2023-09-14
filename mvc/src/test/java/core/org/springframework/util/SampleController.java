package core.org.springframework.util;

import context.org.springframework.stereotype.Controller;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SampleController {

    @RequestMapping(value = "/sample", method = RequestMethod.GET)
    public void sample() {
    }

    public void sample2() {
    }
}
