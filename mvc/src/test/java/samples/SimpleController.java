package samples;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(SimpleController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        log.info("인터페이스 구현체 컨트롤러 실행");
        return "hello, Simple";
    }
}
