package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestController1 {

    private static final Logger log = LoggerFactory.getLogger(TestController1.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView root(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller1 root path invoked");
        return new ModelAndView(new JspView("/"));
    }

    @RequestMapping(value = "/all-method")
    public ModelAndView all(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller1 all method invoked");
        return new ModelAndView(new JspView("/all-method"));
    }
}
