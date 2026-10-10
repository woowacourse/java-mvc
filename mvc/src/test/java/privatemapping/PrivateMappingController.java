package privatemapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PrivateMappingController {

    @RequestMapping("/private-mapping")
    private ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return null;
    }
}
