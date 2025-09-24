package com.interface21.webmvc.servlet.mvc.handler.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.scanner.ControllerScanner;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();

        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        registerHandlerExecutionsByControllers(controllers);
    }

    @Override
    public Optional<Object> getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return Optional.ofNullable(handlerExecutions.get(handlerKey));
    }

    private void registerHandlerExecutionsByControllers(Map<Class<?>, Object> controllers) {
        Set<Entry<Class<?>, Object>> keyValues = controllers.entrySet();
        for (Entry<Class<?>, Object> keyValue : keyValues) {
            Class<?> controllerClass = keyValue.getKey();
            Object controllerInstance = keyValue.getValue();
            List<Method> apiMethods = findApiMethod(controllerClass);
            apiMethods.forEach(method -> registerHandlerExecutionsByMethod(method, controllerInstance));
        }
    }

    private List<Method> findApiMethod(Class<?> controllerClass) {
        return Stream.of(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerHandlerExecutionsByMethod(Method method, Object controller) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        List<RequestMethod> requestMethods = getRequestMethodByRequestMapping(annotation);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<RequestMethod> getRequestMethodByRequestMapping(RequestMapping annotation) {
        List<RequestMethod> requestMethods = List.of(annotation.method());
        if (requestMethods.isEmpty()) {
            return List.of(RequestMethod.values());
        }
        return requestMethods;
    }
}
