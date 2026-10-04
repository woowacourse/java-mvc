package duplicatemapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DuplicateMappingController {

    @RequestMapping(value = "/duplicate-test", method = RequestMethod.GET)
    public ModelAndView firstHandler(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    @RequestMapping(value = "/duplicate-test", method = RequestMethod.GET)
    public ModelAndView secondHandler(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }
}
