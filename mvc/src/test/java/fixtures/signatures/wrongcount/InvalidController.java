package fixtures.signatures.wrongcount;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class InvalidController {

    @RequestMapping("/invalid")
    public ModelAndView handle(HttpServletRequest request) { return null; }
}
