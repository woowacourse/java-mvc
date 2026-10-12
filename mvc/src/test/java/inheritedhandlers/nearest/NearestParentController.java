package inheritedhandlers.nearest;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import inheritedhandlers.base.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class NearestParentController extends MiddleController {
    @Override
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("handler", "nearest-child");
    }
}

class MiddleController extends BaseController {
    @Override
    @RequestMapping(value = "/nearest", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }
}
