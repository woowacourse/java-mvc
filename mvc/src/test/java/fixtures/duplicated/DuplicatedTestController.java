package fixtures.duplicated;

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
public class DuplicatedTestController {

    private static final Logger log = LoggerFactory.getLogger(DuplicatedTestController.class);

    @RequestMapping(value = "/duplicated-test", method = RequestMethod.GET)
    public ModelAndView duplicated1(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller duplicated1 method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping(value = "/duplicated-test", method = RequestMethod.GET)
    public ModelAndView duplicated2(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller duplicated2 method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }
}
