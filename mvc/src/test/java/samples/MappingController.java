package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MappingController {

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findUsers(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("route", "find-users");
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView saveUser(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("route", "save-user");
    }

    @RequestMapping("/all-methods")
    public ModelAndView allMethods(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("route", "all-methods");
    }

    @RequestMapping(value = "/private-test", method = RequestMethod.GET)
    private ModelAndView privateHandler(final HttpServletRequest request, final HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return new ModelAndView(new JspView(""))
                .addObject("route", "private-handler")
                .addObject("id", request.getAttribute("id"));
    }
}
