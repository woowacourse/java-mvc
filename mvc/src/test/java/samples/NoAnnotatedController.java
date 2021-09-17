package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoAnnotatedController {

    private static final Logger log = LoggerFactory.getLogger(NoAnnotatedController.class);

    public String findUserId(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller get method2");
        return "get-test-annotation";
    }
}
