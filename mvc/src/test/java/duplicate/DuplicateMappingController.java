package duplicate;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DuplicateMappingController {

    @RequestMapping("/duplicate")
    public ModelAndView allMethods(final HttpServletRequest request, final HttpServletResponse response) {
        return null;
    }

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView get(final HttpServletRequest request, final HttpServletResponse response) {
        return null;
    }
}
