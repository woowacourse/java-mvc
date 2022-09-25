package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.Controller;

public class TestInterfaceController implements Controller {

    private final String path;

    public TestInterfaceController() {
        this.path = "/test-path.jsp";
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return path;
    }
}
