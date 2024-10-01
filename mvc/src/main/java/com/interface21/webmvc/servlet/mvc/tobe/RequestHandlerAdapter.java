package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.RequestHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class RequestHandlerAdapter implements HandlerAdapter {

    private final List<HandlerMapping> handlerMappings;

    public RequestHandlerAdapter(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<RequestHandler> handlerOptional = getHandler(request);
        if (handlerOptional.isEmpty()) {
            throw new ServletException("No handler found for request URI: " + request.getRequestURI());
        }
        return handlerOptional.get().handle(request, response);
    }

    private Optional<RequestHandler> getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        for (HandlerMapping handlerMapping : handlerMappings) {
            RequestHandler handler = handlerMapping.getHandler(method, requestURI);
            if (handler != null) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
}
