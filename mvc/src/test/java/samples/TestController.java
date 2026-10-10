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
        final var modelAndView = new ModelAndView(new JspView("/get-test.jsp"));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping(value = "/post-test", method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller post method");
        final var modelAndView = new ModelAndView(new JspView("/post-test.jsp"));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping(value = "/method-test", method = RequestMethod.GET)
    public ModelAndView getByMethod(final HttpServletRequest request, final HttpServletResponse response) {
        return modelAndView("get");
    }

    @RequestMapping(value = "/method-test", method = RequestMethod.POST)
    public ModelAndView postByMethod(final HttpServletRequest request, final HttpServletResponse response) {
        return modelAndView("post");
    }

    @RequestMapping("/all-methods")
    public ModelAndView supportAllMethods(final HttpServletRequest request, final HttpServletResponse response) {
        return modelAndView("all");
    }

    @RequestMapping(
            value = "/multiple-methods",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ModelAndView supportMultipleMethods(final HttpServletRequest request, final HttpServletResponse response) {
        return modelAndView("multiple");
    }

    private ModelAndView modelAndView(final String handler) {
        return new ModelAndView(new JspView(""))
                .addObject("handler", handler);
    }

    @Controller
    public static class OverridingController extends ParentController {

        @Override
        @RequestMapping(value = "/overridden", method = RequestMethod.GET)
        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""))
                    .addObject("handler", "child");
        }
    }

    public static class ParentController {

        @RequestMapping(value = "/overridden", method = RequestMethod.GET)
        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""))
                    .addObject("handler", "parent");
        }
    }
}
