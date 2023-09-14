package samples;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

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

    @RequestMapping(value = "/multi-method-test", method = {RequestMethod.GET, RequestMethod.POST} )
    public ModelAndView multiHandle(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller multi-handle method");
        String method = request.getMethod();
        if("GET".equals(method)){
            final var modelAndView = new ModelAndView(new JspView(""));
            modelAndView.addObject("id", "getPooh");
            return modelAndView;
        }
        if("POST".equals(method)){
            final var modelAndView = new ModelAndView(new JspView(""));
            modelAndView.addObject("id", "postPooh");
            return modelAndView;
        }
        throw new IllegalArgumentException("해당 요청을 Handling 할 수 없는 핸들러입니다.");
    }
}
