package invalidsamples.returntype;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class StringReturnController {

    @RequestMapping(value = "/string-return", method = RequestMethod.GET)
    public String stringReturn(final HttpServletRequest request, final HttpServletResponse response) {
        return "index.jsp";
    }
}
