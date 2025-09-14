package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object basePkg : basePackage) {
            Set<Class<?>> controllers = findControllers(basePkg);
            controllers.forEach(controller -> {
                Object instance = toInstance(controller);
                Map<HandlerKey, HandlerExecution> handlerMapping = mapHandlerMethods(instance);
                handlerExecutions.putAll(handlerMapping);

                handlerMapping.forEach((key, execution) -> log.info("Mapped {}:{}", key, execution));
            });
        }
    }

    private Set<Class<?>> findControllers(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Object toInstance(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate controller: " + controller, e);
        }
    }

    private Map<HandlerKey, HandlerExecution> mapHandlerMethods(Object controller) {
        Map<HandlerKey, HandlerExecution> result = new HashMap<>();
        for (Method method : controller.getClass().getDeclaredMethods()) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            if (rm != null) {
                String url = rm.value();
                RequestMethod[] requestMethods = getAppliedRequestMethods(rm);

                for (RequestMethod requestMethod : requestMethods) {
                    HandlerKey key = new HandlerKey(url, requestMethod);
                    HandlerExecution execution = new HandlerExecution(controller, method);
                    result.put(key, execution);
                }
            }
        }
        return result;
    }

    private RequestMethod[] getAppliedRequestMethods(RequestMapping rm) {
        RequestMethod[] requestMethods = rm.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, requestMethod);

        return handlerExecutions.get(key);
    }
}
