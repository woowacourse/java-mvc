package com.techcourse;

import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/register/view", new RegisterViewController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                   .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    public Controller getHandler(final HttpServletRequest request) {
        log.info("Request Mapping Uri : {}", request.getRequestURI());
        return controllers.get(request.getRequestURI());
    }

    @Override
    public boolean containsHandler(final HttpServletRequest request) {
        return Objects.nonNull(controllers.get(request.getRequestURI()));
    }
}
