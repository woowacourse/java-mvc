package inheritedhandlers.unannotated;

import com.interface21.context.stereotype.Controller;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import inheritedhandlers.base.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InheritedMappingController extends BaseController {

    @Override
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("handler", "inherited-child");
    }
}

