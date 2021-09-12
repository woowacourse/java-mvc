package samples;

import static nextstep.web.support.RequestMethod.GET;
import static nextstep.web.support.RequestMethod.POST;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/get-test", method = GET)
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("test controller get method");

        final ModelAndView modelAndView = new ModelAndView(new JspView(request.getRequestURI()));
        modelAndView.addObject("id", request.getAttribute("id"));

        return modelAndView;
    }

    @RequestMapping(value = "/post-test", method = POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("test controller post method");

        final ModelAndView modelAndView = new ModelAndView(new JspView(request.getRequestURI()));
        modelAndView.addObject("id", request.getAttribute("id"));

        return modelAndView;
    }
}
