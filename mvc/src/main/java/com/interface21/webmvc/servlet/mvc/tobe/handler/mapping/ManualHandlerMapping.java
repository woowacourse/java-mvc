package com.interface21.webmvc.servlet.mvc.tobe.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    public Controller getHandler(final String requestURI) {
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return this.getHandler(request.getRequestURI());
    }
}
