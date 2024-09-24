package com.interface21.webmvc.servlet.mvc.tobe;

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
                .orElseThrow(() -> new IllegalArgumentException("처리할 수 있는 핸들러가 존재하지 않습니다."))
                .handle(request, response);
    }

    public void initialize() {
        servletRequestHandlers.forEach(ServletRequestHandler::initialize);
    }
}
