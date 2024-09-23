package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {


    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<RequestMapping> REQUEST_MAPPING_MARKER = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Object instance = entry.getValue();
            List<Method> handlerMethods = getRequestMappingMethods(entry.getKey());
            handlerMethods.forEach(method -> putAllMatchedHandlerExecutionOf(instance, method));
        }

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet()
                .forEach(handlerKey -> log.info("HandlerKey : {}", handlerKey));
    }

    private List<Method> getRequestMappingMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(REQUEST_MAPPING_MARKER))
                .toList();
    }

    private void putAllMatchedHandlerExecutionOf(Object instance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(REQUEST_MAPPING_MARKER);
        String path = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (hasNoHttpMethods(requestMethods)) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(instance, method));
        }
    }

    private boolean hasNoHttpMethods(RequestMethod[] requestMethods) {
        return requestMethods.length == 0;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        log.debug("(Annotation) Request Mapping Uri : {}", request.getRequestURI());
        HandlerKey handlerKey = HandlerKey.from(request);
        return handlerExecutions.get(handlerKey);
    }
}
