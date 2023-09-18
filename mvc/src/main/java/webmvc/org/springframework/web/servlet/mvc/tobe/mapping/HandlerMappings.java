package webmvc.org.springframework.web.servlet.mvc.tobe.mapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {


    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) throws ServletException {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new ServletException("Not found handler for request URI : " + request.getRequestURI());
    }
}
