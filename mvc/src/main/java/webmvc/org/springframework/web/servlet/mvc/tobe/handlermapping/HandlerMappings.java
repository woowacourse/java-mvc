package webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Optional<Object> handler = handlerMapping.getHandler(request);
            if (handler.isPresent()) {
                return handler.get();
            }
        }
        throw new HandlerNotExistException();
    }
}
