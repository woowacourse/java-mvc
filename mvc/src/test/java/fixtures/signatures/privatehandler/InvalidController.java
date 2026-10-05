package fixtures.signatures.privatehandler;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidController {

    @RequestMapping("/invalid")
    private ModelAndView handle(HttpServletRequest request, HttpServletResponse response) { return null; }
}
