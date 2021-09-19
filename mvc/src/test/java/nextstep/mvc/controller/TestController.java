package nextstep.mvc.controller;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class TestController {

    @RequestMapping(value = "/test-get", method = RequestMethod.GET)
    public void testGet() {
    }

    @RequestMapping(value = "/test-post", method = RequestMethod.POST)
    public void testPost() {
    }
}
