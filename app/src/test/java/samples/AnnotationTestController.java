package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;

@Controller
public class AnnotationTestController {

    @RequestMapping(value = "/all-test")
    public ModelAndView any(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
