package webmvc.org.springframework.web.servlet.mvc.mapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) throws ServletException {
        return handlerMappings.stream()
                .map(it -> it.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new ServletException("Not found handler for request URI : " + request.getRequestURI()));
    }
}
