package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        handlerExecutions.clear();
        final var controllers = new ControllerScanner(basePackage).getControllers();

        for (var entry : controllers.entrySet()) {
            log.info("Found controller: {}", entry.getKey().getName());

            for (var method : entry.getKey().getDeclaredMethods()) {
                final var requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }

                var httpMethods = requestMapping.method();
                if (httpMethods.length == 0) {
                    httpMethods = RequestMethod.values();
                }

                final var execution = new HandlerExecution(entry.getValue(), method);
                for (var httpMethod : httpMethods) {
                    final var key = new HandlerKey(requestMapping.value(), httpMethod);
                    handlerExecutions.put(key, execution);
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final var method = RequestMethod.valueOf(request.getMethod());
        final var key = new HandlerKey(request.getRequestURI(), method);
        return handlerExecutions.get(key);
    }
}
