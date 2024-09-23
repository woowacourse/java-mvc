package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public HandlerExecutions() {
        this.handlerExecutions = new HashMap<>();
    }

    public void add(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Optional<HandlerExecution> findHandler(HttpServletRequest request) {
        return Optional.ofNullable(handlerExecutions.get(
                        new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))
                )
        );
    }
}
