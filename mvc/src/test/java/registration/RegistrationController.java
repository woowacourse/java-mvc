package registration;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegistrationController {

    @RequestMapping(value = "/same", method = RequestMethod.GET)
    public ModelAndView get(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("method", "get");
    }

    @RequestMapping(value = "/same", method = RequestMethod.POST)
    public ModelAndView post(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("method", "post");
    }

    @RequestMapping(value = "/multiple", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView multiple(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("method", "multiple");
    }

    @RequestMapping("/all")
    public ModelAndView all(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("method", "all");
    }

    public ModelAndView unmapped(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("method", "unmapped");
    }

    @RequestMapping("/private")
    private ModelAndView privateHandler(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    @RequestMapping("/protected")
    protected ModelAndView protectedHandler(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    @RequestMapping("/package-private")
    ModelAndView packagePrivateHandler(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }
}
