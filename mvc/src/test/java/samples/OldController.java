package samples;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OldController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        return "viewName";
    }
}
