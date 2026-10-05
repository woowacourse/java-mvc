package fixtures.signatures.wrongtype;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidController {

    @RequestMapping("/invalid")
    public ModelAndView handle(String request, HttpServletResponse response) { return null; }
}
