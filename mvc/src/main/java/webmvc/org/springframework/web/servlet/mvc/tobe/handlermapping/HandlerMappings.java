package webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotExistException;

public class HandlerMappings {

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void initialize() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerMapping handlerMapping = handlerMappings.stream()
            .filter(mapping -> mapping.isHandleable(request))
            .findFirst()
            .orElseThrow(HandlerNotExistException::new);

        return handlerMapping.getHandler(request);
    }
}
