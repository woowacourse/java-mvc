package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.getControllers();

        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> controllerClass = entry.getKey();
            Object controller = entry.getValue();

            for (Method declaredMethod : controllerClass.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);

                    String value = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();
                    if (requestMethods.length == 0) {
                        requestMethods = RequestMethod.values();
                    }

                    HandlerExecution execution = new HandlerExecution(controller, declaredMethod);

                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey key = new HandlerKey(value, requestMethod);

                        if (handlerExecutions.containsKey(key)) {
                            throw new IllegalStateException("중복된 요청 매핑입니다: " + key);
                        }
                        handlerExecutions.put(key, execution);
                    }
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Optional<HandlerExecution> getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        RequestMethod requestMethod = RequestMethod.valueOf(method);
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return Optional.ofNullable(handlerExecutions.get(handlerKey));
    }
}
