package mappingfixtures.collisions.defaults;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DefaultMethodController {

    @RequestMapping("/duplicate")
    public ModelAndView handleAll(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/all.jsp"));
    }

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView handleGet(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/get.jsp"));
    }
}
