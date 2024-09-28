package samples;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestControllerInterfaceController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(TestControllerInterfaceController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        log.info("test controller post method");

        return "controller based handler post";
    }
}
