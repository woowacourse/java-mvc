package invalidhandlers.visibility;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class NonPublicController {

    @RequestMapping("/non-public")
    private ModelAndView invalid(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
