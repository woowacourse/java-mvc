package nextstep.mvc.controller.tobe.fixture;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class AnnotationController {

    public static final String url = "/test";

    @RequestMapping(value = url, method = RequestMethod.GET)
    public void get() {
    }
}
