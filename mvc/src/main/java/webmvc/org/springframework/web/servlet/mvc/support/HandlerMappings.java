package webmvc.org.springframework.web.servlet.mvc.support;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private boolean isInitialize;

    public void initialize() {
        if (isInitialize) {
            throw new IllegalStateException("handlerMappings already initialize!");
        }
        handlerMappings.forEach(HandlerMapping::initialize);
        isInitialize = true;
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Nonnull
    public Object getHandler(HttpServletRequest request) {
        validate(request);
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findAny()
            .orElseThrow(() -> {
                String uri = request.getRequestURI();
                String method = request.getMethod();
                throw new HandlerNotFoundException("handler not found! uri: " + uri + ", method: " + method);
            });
    }

    private void validate(HttpServletRequest request) {
        if (!isInitialize) {
            throw new IllegalStateException("handlerMappings not initialize!");
        }
        if (request == null) {
            throw new IllegalArgumentException("httpServletReqeust is null!");
        }
    }
}
