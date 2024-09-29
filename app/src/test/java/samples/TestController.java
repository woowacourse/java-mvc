package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestController {

    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return "/test.jsp";
    }
}
