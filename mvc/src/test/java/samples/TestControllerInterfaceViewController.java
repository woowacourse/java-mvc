package samples;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestControllerInterfaceViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(TestControllerInterfaceViewController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        log.info("test controller get method");

        return "controller based handler get";
    }
}
