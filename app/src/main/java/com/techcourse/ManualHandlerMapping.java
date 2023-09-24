package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        try {
            final String requestUri = request.getRequestURI();
            final Controller controller = controllers.get(requestUri);
            return getHandlerExecution(controller);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private HandlerExecution getHandlerExecution(final Controller controller) throws NoSuchMethodException {
        return Optional.ofNullable(controller)
                .map(instance -> {
                    final var clazz = instance.getClass();
                    final var method = getExecution(clazz);
                    return new HandlerExecution(controller, method);
                })
                .orElse(null);
    }

    private Method getExecution(final Class<? extends Controller> clazz) {
        try {
            return clazz.getMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
