package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = ControllerScanner.of(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Class<?> controllerClass : controllers.keySet()) {
            Method[] methods = controllerClass.getMethods();
            addHandlerMappings(controllers.get(controllerClass), methods);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        return handlerExecutions.get(handlerKey);
    }

    private void addHandlerMappings(Object controller, Method[] methods) {
        for(Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                registerRequestMapping(controller, method, requestMapping);
            }
        }
    }

    private void registerRequestMapping(Object controller, Method method, RequestMapping requestMapping) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            String value = requestMapping.value();
            handlerExecutions.put(
                    new HandlerKey(value, requestMethod),
                    new HandlerExecution(controller, method)
            );
        }
    }
}
