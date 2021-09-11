package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.UnHandledRequestException;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.ExceptionHandler;
import nextstep.web.annotation.SessionAttribute;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestParam;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller get method");
        final ModelAndView modelAndView = new ModelAndView("");
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping(value = "/post-test", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller post method");
        final ModelAndView modelAndView = new ModelAndView("");
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @ExceptionHandler(value = UnHandledRequestException.class)
    public ModelAndView notFoundException(Exception e) {
        return new ModelAndView("404.html");
    }
}
