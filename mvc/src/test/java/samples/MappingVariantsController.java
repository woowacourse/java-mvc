package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MappingVariantsController {

    @RequestMapping(value = "/same-test", method = RequestMethod.GET)
    public ModelAndView get(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("method", "GET");
    }

    @RequestMapping(value = "/same-test", method = RequestMethod.POST)
    public ModelAndView post(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("method", "POST");
    }

    @RequestMapping("/any-method")
    public ModelAndView anyMethod(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }
}
