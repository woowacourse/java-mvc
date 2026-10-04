package invalidhandlers.parameters;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;

@Controller
public class InvalidParametersController {

    @RequestMapping("/invalid-parameters")
    public ModelAndView invalid(String account) {
        return null;
    }
}
