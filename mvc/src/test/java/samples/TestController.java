package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        log.info("test controller get method");
        return "/index.jsp";
    }
}
