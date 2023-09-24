package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotFoundException;

public class HandlerMappings {

    private static final String CONTROLLER_BASE_PACKAGE = "com";

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void init() {
        handlerMappings.add(new AnnotationHandlerMapping(CONTROLLER_BASE_PACKAGE));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerExecution handler;
        for (HandlerMapping handlerMapping : handlerMappings) {
            handler = handlerMapping.getHandler(request);
            if (Objects.nonNull(handler)) {
                return handler;
            }
        }
        throw new HandlerNotFoundException();
    }
}
