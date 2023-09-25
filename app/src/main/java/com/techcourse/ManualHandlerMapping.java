package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.Handler;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.MannualHandler;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private final Map<String, MannualHandler> handlers = new HashMap<>();

    @Override
    public void initialize() {
        log.info("Initialized Handler Mapping!");
        handlers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, handlers.get(path).getController().getClass()));
    }

    @Override
    public Handler getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return handlers.get(requestURI);
    }
}
