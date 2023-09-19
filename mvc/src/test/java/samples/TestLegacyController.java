package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class TestLegacyController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return "/register.jsp";
    }
}
