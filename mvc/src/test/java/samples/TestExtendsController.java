package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;

public class TestExtendsController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "/test.jsp";
    }
}
