package samples;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSimpleController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(TestSimpleController.class);

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        log.info("test simple controller method");
        return "test.jsp";
    }
}
