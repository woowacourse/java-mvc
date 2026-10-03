package wrongsamples.invalidparameter;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidParameterController {

    @RequestMapping("/invalid-parameter")
    public ModelAndView handle(HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }
}
