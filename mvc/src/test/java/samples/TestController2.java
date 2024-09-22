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

@Controller(value = "/hello")
public class TestController2 {

    private static final Logger log = LoggerFactory.getLogger(TestController2.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getParentPath(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller2 parent path invoked");
        return new ModelAndView(new JspView("/hello"));
    }

    @RequestMapping(value = "/world", method = RequestMethod.GET)
    public ModelAndView getCombinedPath(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller2 combined path invoked");
        return new ModelAndView(new JspView("/hello/world"));
    }
}
