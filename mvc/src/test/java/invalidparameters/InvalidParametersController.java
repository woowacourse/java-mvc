package invalidparameters;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class InvalidParametersController {

    @RequestMapping("/invalid-parameters")
    public ModelAndView show(final HttpServletRequest request) {
        return null;
    }
}
