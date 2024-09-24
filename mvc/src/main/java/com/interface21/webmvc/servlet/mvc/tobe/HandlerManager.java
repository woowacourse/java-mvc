package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.exception.HandlerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerManager {
    List<ServletRequestHandler> servletRequestHandlers;

    public HandlerManager(ServletRequestHandler... servletRequestHandlers) {
        this.servletRequestHandlers = List.of(servletRequestHandlers);
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        servletRequestHandlers.stream()
                .filter(servletRequestHandler -> servletRequestHandler.canHandle(request))
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException(request))
                .handle(request, response);
    }

    public void initialize() {
        servletRequestHandlers.forEach(ServletRequestHandler::initialize);
    }
}
