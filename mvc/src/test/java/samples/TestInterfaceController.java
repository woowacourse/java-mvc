package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.techcourse.air.mvc.core.controller.asis.Controller;

public class TestInterfaceController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return "/login.jsp";
    }
}
