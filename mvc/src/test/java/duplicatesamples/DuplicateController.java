package duplicatesamples;

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
public class DuplicateController {

    private static final Logger log = LoggerFactory.getLogger(DuplicateController.class);

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView duplicateFirst(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView duplicateSecond(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller post method");
        return new ModelAndView(new JspView(""));
    }
}
