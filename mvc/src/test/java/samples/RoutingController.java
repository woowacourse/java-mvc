package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RoutingController {

    @RequestMapping("/all-methods")
    public ModelAndView allMethods(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/test.jsp"))
                .addObject("method", request.getMethod());
    }

    @RequestMapping(value = "/shared-url", method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/test.jsp")).addObject("method", "GET");
    }

    @RequestMapping(value = "/shared-url", method = RequestMethod.POST)
    public ModelAndView post(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/test.jsp")).addObject("method", "POST");
    }
}
