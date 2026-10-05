package fixtures.duplicates.any;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DuplicateController {

    @RequestMapping("/duplicate")
    public ModelAndView first(final HttpServletRequest request, final HttpServletResponse response) {
        return null;
    }

    @RequestMapping("/duplicate")
    public ModelAndView second(final HttpServletRequest request, final HttpServletResponse response) {
        return null;
    }
}
