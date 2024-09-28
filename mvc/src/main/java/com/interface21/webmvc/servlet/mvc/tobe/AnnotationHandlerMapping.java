package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackages);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> controller : controllers.keySet()) {
            Set<Method> methods = getRequestMappingMethods(controller);
            methods.forEach(method -> addHandlerExecution(controllers.get(controller), method));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Method> getRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecution(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = getRequestMappingMethod(requestMapping);
        HandlerExecution handlerExecution = new HandlerExecution(method, controllerInstance);
        Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    private RequestMethod[] getRequestMappingMethod(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
