package invalidsamples.parameter;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SingleParameterController {

    @RequestMapping(value = "/single-parameter", method = RequestMethod.GET)
    public ModelAndView singleParameter(final HttpServletRequest request) {
        return new ModelAndView(new JspView(""));
    }
}
