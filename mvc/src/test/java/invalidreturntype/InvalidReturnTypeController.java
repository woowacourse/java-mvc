package invalidreturntype;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidReturnTypeController {

    @RequestMapping(value = "/invalid-return-type", method = RequestMethod.GET)
    public String viewName(final HttpServletRequest request, final HttpServletResponse response) {
        return "/index.jsp";
    }
}
