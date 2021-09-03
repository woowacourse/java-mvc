package nextstep.mvc.controller.scanner.test_classes;

import static nextstep.web.support.RequestMethod.GET;
import static nextstep.web.support.RequestMethod.POST;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;

@Controller
public class Test1 {

    @RequestMapping(value = "/test1", method = GET)
    public void test1() {
    }


    @RequestMapping(value = "/test2", method = {GET, POST})
    public void test2() {
    }

    public void test3() {
    }
}
