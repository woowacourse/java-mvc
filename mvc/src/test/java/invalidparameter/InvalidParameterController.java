package invalidparameter;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class InvalidParameterController {

    @RequestMapping(value = "/invalid-parameter", method = RequestMethod.GET)
    public ModelAndView withoutResponse(final HttpServletRequest request) {
        return new ModelAndView(new JspView(""));
    }
}
