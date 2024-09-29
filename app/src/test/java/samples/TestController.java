package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.mvc.asis.Controller;

public class TestController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return "/test.jsp";
    }
}
