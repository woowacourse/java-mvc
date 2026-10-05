package fixtures.signatures.wrongorder;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidController {

    @RequestMapping("/invalid")
    public ModelAndView handle(HttpServletResponse response, HttpServletRequest request) { return null; }
}
