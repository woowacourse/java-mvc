package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class TestManualController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        return "/tests";
    }
}
