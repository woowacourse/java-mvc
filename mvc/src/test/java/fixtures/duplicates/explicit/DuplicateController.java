package fixtures.duplicates.explicit;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DuplicateController {

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView first(final HttpServletRequest request, final HttpServletResponse response) {
        return null;
    }

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView second(final HttpServletRequest request, final HttpServletResponse response) {
        return null;
    }
}
