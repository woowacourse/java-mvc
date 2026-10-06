package mappingfixtures;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MethodMappingController {

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/users.jsp")).addObject("handler", "show");
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/users.jsp")).addObject("handler", "save");
    }

    @RequestMapping(value = "/multi", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView shared(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/multi.jsp")).addObject("handler", "shared");
    }
}
