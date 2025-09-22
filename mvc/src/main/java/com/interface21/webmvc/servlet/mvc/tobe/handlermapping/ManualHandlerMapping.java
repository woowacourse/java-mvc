package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private final Map<String, Controller> controllers;

    public ManualHandlerMapping(final Map<String, Controller> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void initialize() {
        log.info("Initialized ManualHandlerMapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public boolean support(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        return controllers.containsKey(requestURI);
    }

    @Override
    public Controller getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }
}
