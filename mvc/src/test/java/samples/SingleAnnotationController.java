package samples;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller("single")
public class SingleAnnotationController {

    @RequestMapping(method = RequestMethod.GET)
    public void get() {
        // NOOP
    }

    @RequestMapping(value = "mapping", method = RequestMethod.GET)
    public void getWithAdditionalValue() {
        // NOOP
    }
}
