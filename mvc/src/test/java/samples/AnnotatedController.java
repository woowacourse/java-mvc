package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AnnotatedController {

    private static final Logger log = LoggerFactory.getLogger(AnnotatedController.class);

    @RequestMapping(value = "/get-test-annotation", method = RequestMethod.GET)
    public String findUserId(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller get method2");
        return "get-test-annotation";
    }

    @RequestMapping(value = "/post-test-annotation", method = RequestMethod.POST)
    public String save2(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        log.info("test controller post method2");
        modelAndView.addObject("id", request.getAttribute("id"));
        return "post-test-annotation";
    }
}
