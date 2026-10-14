package mappingfixtures.invalid;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidController {

    @RequestMapping(value = "/invalid", method = RequestMethod.GET)
    public String show(HttpServletRequest request, HttpServletResponse response) {
        return "invalid.jsp";
    }
}
