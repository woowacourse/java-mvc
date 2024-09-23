package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerManager {

    List<Handler> handlers;

    public HandlerManager(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        handlers.stream()
                .filter(handler -> handler.canHandle(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("처리할 수 있는 핸들러가 존재하지 않습니다."))
                .handle(request, response);
    }

    public void initialize() {
        handlers.forEach(Handler::initialize);
    }
}
