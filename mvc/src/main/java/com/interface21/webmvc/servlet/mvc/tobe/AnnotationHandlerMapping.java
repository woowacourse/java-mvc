package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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
        ControllerScanner controllerScanner = new ControllerScanner();
        Map<Class<?>, Object> controllers = controllerScanner.scan(basePackage);

        controllers.forEach(this::registerController);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(Class<?> controllerType, Object controller) {
        for (Method method : controllerType.getDeclaredMethods()) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            if (mapping != null) {
                registerHandlerMethod(method, controller, mapping);
            }
        }
    }

    private void registerHandlerMethod(Method method, Object controller, RequestMapping mapping) {
        makeAccessible(method);
        HandlerExecution execution = new HandlerExecution(controller, method);

        RequestMethod[] requestMethods = mapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(mapping.value(), requestMethod);

            HandlerExecution previous = handlerExecutions.putIfAbsent(key, execution);
            if (previous != null) {
                throw new IllegalStateException("중복 요청 매핑: " + key);
            }
        }
    }

    private void makeAccessible(Method method) {
        if (!method.trySetAccessible()) {
            throw new IllegalStateException("@RequestMapping 메서드에 접근할 수 없습니다: " + method);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(key);
    }
}
