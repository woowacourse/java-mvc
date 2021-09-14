package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class Test2Controller {
    private static final Logger log = LoggerFactory.getLogger(Test2Controller.class);

    @RequestMapping(value = "/get-test2", method = RequestMethod.GET)
    public String findUserId2(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller get method2");
        return "test controller get method2";
    }

    public String save2(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller post method2");
        return "test controller post method2";
    }
}
