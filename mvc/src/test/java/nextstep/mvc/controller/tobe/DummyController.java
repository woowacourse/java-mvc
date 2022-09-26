package nextstep.mvc.controller.tobe;

import static nextstep.web.support.RequestMethod.GET;

import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;

@Controller
public class DummyController {

    @RequestMapping(value = "/dummy", method = {GET})
    public ModelAndView dummy() {
        return null;
    }
}
