package invalidsamples.extraparameter;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ExtraParameterController {

    @RequestMapping(value = "/extra-parameter", method = RequestMethod.GET)
    public ModelAndView extraParameter(final HttpServletRequest request, final HttpServletResponse response, final String extra) {
        return new ModelAndView(new JspView(""));
    }
}
