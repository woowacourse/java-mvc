package common;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;

public class FakeManualHandlerMapping implements HandlerMapping {

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
    }

    @Override
    public Optional<Object> getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        return Optional.ofNullable(controllers.get(requestURI));
    }
}
