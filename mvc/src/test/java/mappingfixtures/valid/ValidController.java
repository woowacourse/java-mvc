package mappingfixtures.valid;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ValidController {

    @RequestMapping(value = "/shared", method = {RequestMethod.GET, RequestMethod.GET})
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/shared.jsp")).addObject("method", "GET");
    }

    @RequestMapping(value = "/shared", method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/shared.jsp")).addObject("method", "POST");
    }
}
