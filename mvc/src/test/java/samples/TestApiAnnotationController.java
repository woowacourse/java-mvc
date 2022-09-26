package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class TestApiAnnotationController {

    @RequestMapping(value = "/api/sample", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) {
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("data", new DataExample(1L, "sample"));
        return modelAndView;
    }
}
