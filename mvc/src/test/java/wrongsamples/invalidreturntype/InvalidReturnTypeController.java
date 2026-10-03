package wrongsamples.invalidreturntype;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvalidReturnTypeController {

    @RequestMapping("/invalid-return-type")
    public String handle(HttpServletRequest request, HttpServletResponse response) {
        return "잘못된 반환 타입";
    }
}
