package invalidmappingfixtures.returntype;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidReturnTypeController {

    @RequestMapping("/invalid")
    public String handle(final HttpServletRequest request, final HttpServletResponse response) {
        return "/invalid.jsp";
    }
}
