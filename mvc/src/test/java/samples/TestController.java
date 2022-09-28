package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
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

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView user(HttpServletRequest request, HttpServletResponse response) {
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", new User("verus", 28));
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest request, HttpServletResponse response) {
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user1", new User("verus", 28));
        modelAndView.addObject("user2", new User("gugu", 30));
        return modelAndView;
    }

    @RequestMapping(value = "/api/empty", method = RequestMethod.GET)
    public ModelAndView empty(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JsonView());
    }
}
