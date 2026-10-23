package mappingfixtures.collisions.overloaded;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DuplicateController {

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/first.jsp"));
    }

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView handle(final HttpServletResponse response, final HttpServletRequest request) {
        return new ModelAndView(new JspView("/second.jsp"));
    }
}
