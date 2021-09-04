package samples2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestController3 {

    private static final Logger log = LoggerFactory.getLogger(TestController3.class);

    @RequestMapping(value = "/get-test3", method = RequestMethod.GET)
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller get method3");
        final ModelAndView modelAndView = new ModelAndView(new JspView(request.getRequestURI()));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping(value = "/post-test3", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller post method3");
        final ModelAndView modelAndView = new ModelAndView(new JspView(request.getRequestURI()));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }
}
