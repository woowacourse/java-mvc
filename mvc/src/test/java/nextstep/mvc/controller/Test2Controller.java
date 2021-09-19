package nextstep.mvc.controller;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class Test2Controller {

    @RequestMapping(value = "/test2-get", method = RequestMethod.GET)
    public void test2Get() {
    }

    @RequestMapping(value = "/test2-post", method = RequestMethod.POST)
    public void test2Post() {
    }
}
