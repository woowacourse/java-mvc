package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestManualController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(TestManualController.class);

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) {
        return "redirect:/index.jsp";
    }
}
