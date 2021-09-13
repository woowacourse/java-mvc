package samples;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@RequestMapping("/default-url")
@Controller
public class RequestMappingController {

    @RequestMapping(value = "/detailed-url", method = RequestMethod.GET)
    public void get() {
        // NOOP
    }
}
