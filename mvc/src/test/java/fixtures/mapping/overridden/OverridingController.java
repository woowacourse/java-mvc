package fixtures.mapping.overridden;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import fixtures.mapping.inherited.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class OverridingController extends BaseController {

    @Override
    @RequestMapping(value = "/inherited", method = RequestMethod.GET)
    public ModelAndView inherited(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/overridden.jsp")).addObject("handler", "overridden");
    }
}
