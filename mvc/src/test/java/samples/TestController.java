package samples;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public String findUserId(final HttpServletRequest request, final Map<String, Object> model) {
        log.info("test controller get method");
        model.put("id", request.getAttribute("id"));
        return "/";
    }

    @RequestMapping(value = "/post-test", method = RequestMethod.POST)
    public String save(final HttpServletRequest request, final Map<String, Object> model) {
        log.info("test controller post method");
        model.put("id", request.getAttribute("id"));
        return "/";
    }

    @RequestMapping(value = "/no-method-test")
    public String noMethodController(final HttpServletRequest request, final Map<String, Object> model) {
        log.info("test controller no method");
        model.put("id", request.getAttribute("id"));
        return "/";
    }
}
