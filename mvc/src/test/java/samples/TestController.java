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
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public String findUserId(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller get method");
        return "test controller get method";
    }

    @RequestMapping(value = "/post-test", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        log.info("test controller post method");
        modelAndView.addObject("id", request.getAttribute("id"));
        return "test controller post method";
    }
}
