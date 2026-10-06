package invalidhandlers.returntype;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidReturnTypeController {

    @RequestMapping("/invalid-return-type")
    public String invalid(HttpServletRequest request, HttpServletResponse response) {
        return "invalid";
    }
}
