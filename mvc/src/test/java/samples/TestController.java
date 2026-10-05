package samples;

import com.interface21.context.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

@Controller
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public ModelAndView findUserId(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller get method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping(value = "/post-test", method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller post method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping("/all-methods")
    public ModelAndView handleAllMethods(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    @RequestMapping(value = "/same-url", method = RequestMethod.GET)
    public ModelAndView handleGet(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("handler", "GET");
    }

    @RequestMapping(value = "/same-url", method = RequestMethod.POST)
    public ModelAndView handlePost(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("handler", "POST");
    }

    @RequestMapping("/overlapping")
    public ModelAndView handleFallback(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("handler", "공통");
    }

    @RequestMapping(value = "/overlapping", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleExplicitMethods(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("handler", "명시적");
    }
}
