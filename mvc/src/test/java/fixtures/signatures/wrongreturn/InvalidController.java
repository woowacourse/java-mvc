package fixtures.signatures.wrongreturn;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidController {

    @RequestMapping("/invalid")
    public String handle(HttpServletRequest request, HttpServletResponse response) { return "invalid"; }
}
