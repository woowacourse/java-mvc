package samples;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@RequestMapping("/both")
@Controller("/both-fake")
public class BothAnnotationController {

    @RequestMapping(method = RequestMethod.GET)
    public void get() {
        // NOOP
    }
}
