package mappingfixtures.fallback;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FallbackController {

    @RequestMapping(value = "/shared", method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/shared.jsp")).addObject("source", "get");
    }

    @RequestMapping("/shared")
    public ModelAndView anyMethod(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/shared.jsp")).addObject("source", "any");
    }
}
