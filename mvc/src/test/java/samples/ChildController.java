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
public class ChildController extends TestController {

    private static final Logger log = LoggerFactory.getLogger(ChildController.class);

    @RequestMapping(value = "/child", method = RequestMethod.GET)
    public ModelAndView child(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("handler", "child");
    }

    @Override
    public ModelAndView overridden(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("child controller overridden method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }
}
