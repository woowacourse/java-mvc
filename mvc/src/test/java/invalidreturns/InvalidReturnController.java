package invalidreturns;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidReturnController {

    @RequestMapping(value = "/invalid-return", method = RequestMethod.GET)
    public String invalid(final HttpServletRequest request, final HttpServletResponse response) {
        return "/invalid.jsp";
    }
}
