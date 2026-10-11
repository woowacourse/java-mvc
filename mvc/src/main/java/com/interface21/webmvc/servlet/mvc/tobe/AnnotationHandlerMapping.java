package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

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
            registerHandlers(entry.getKey(), entry.getValue());
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(final Class<?> controllerClass, final Object controller) {
        for (var method : controllerClass.getDeclaredMethods()) {
            final var requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            registerHandler(controller, method, requestMapping);
        }
    }

    private void registerHandler(final Object controller, final Method method, final RequestMapping requestMapping) {
        var httpMethods = requestMapping.method();
        if (httpMethods.length == 0) {
            httpMethods = RequestMethod.values();
        }

        final var execution = new HandlerExecution(controller, method);
        for (var httpMethod : httpMethods) {
            final var key = new HandlerKey(requestMapping.value(), httpMethod);
            if (handlerExecutions.containsKey(key)) {
                throw new IllegalStateException("중복된 요청 매핑: " + key);
            }

            handlerExecutions.put(key, execution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var method = RequestMethod.valueOf(request.getMethod());
        final var key = new HandlerKey(request.getRequestURI(), method);
        return handlerExecutions.get(key);
    }
}
