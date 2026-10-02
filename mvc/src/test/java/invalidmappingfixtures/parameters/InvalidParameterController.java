package invalidmappingfixtures.parameters;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

@Controller
public class InvalidParameterController {

    @RequestMapping("/invalid")
    public ModelAndView handle() {
        return new ModelAndView(new JspView("/invalid.jsp"));
    }
}
