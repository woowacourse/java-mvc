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
    private final ModelAndView index = new ModelAndView(new JspView("/index.jsp"));

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

    public ModelAndView reversed(HttpServletResponse response, HttpServletRequest request) {
        return new ModelAndView(new JspView("/index.jsp"))
                .addObject("request", request)
                .addObject("response", response);
    }

    public ModelAndView noParameters() {
        return new ModelAndView(new JspView("/index.jsp"));
    }

    public ModelAndView requestOnly(HttpServletRequest request) {
        return new ModelAndView(new JspView("/index.jsp")).addObject("request", request);
    }

    public ModelAndView responseOnly(HttpServletResponse response) {
        return new ModelAndView(new JspView("/index.jsp")).addObject("response", response);
    }

    public ModelAndView requestAndResponse(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/index.jsp"))
                .addObject("request", request)
                .addObject("response", response);
    }

    public String stringViewName() {
        return "/index.jsp";
    }

    public ModelAndView modelAndView() {
        return index;
    }

    public String unsupportedParameter(String value) {
        return "/index.jsp";
    }

    public int unsupportedReturnValue() {
        return 1;
    }

    public ModelAndView nullReturnValue() {
        return null;
    }
}
