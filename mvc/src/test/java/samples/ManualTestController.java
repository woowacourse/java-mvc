package samples;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualTestController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(ManualTestController.class);

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) {
        log.info("test controller manual controller");
        return "/index.jsp";
    }
}
