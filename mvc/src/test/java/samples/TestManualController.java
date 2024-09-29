package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.mvc.asis.Controller;

public class TestManualController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(TestManualController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        log.info("test controller get method");
        return "/";
    }
}
