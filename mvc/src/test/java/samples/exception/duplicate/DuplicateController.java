package samples.exception.duplicate;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import samples.success.TestController;

@Controller
public class DuplicateController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public ModelAndView findUserId(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("duplicate controller get method");
        return new ModelAndView(new JspView(""));
    }

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("duplicate controller get method");
        return new ModelAndView(new JspView(""));
    }
}
