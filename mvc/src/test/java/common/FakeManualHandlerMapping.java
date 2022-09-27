package common;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.asis.Controller;

public class FakeManualHandlerMapping implements HandlerMapping {

    private final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
    }

    @Override
    public Optional<Object> getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return Optional.ofNullable(controllers.get(requestURI));
    }
}
