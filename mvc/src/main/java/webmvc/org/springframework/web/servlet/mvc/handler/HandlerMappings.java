package webmvc.org.springframework.web.servlet.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import web.org.springframework.http.exception.MethodNotAllowedException;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object mapHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.getHandler(request) != null)
                .findFirst()
                .orElseThrow(() -> new MethodNotAllowedException(request.getMethod()))
                .getHandler(request);
    }
}
