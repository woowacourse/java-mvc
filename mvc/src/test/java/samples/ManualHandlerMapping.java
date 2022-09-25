package samples;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.asis.Controller;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new TestImplementController());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        return controllers.get(requestURI);
    }
}
