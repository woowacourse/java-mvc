package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Class<?>> controllerClasses = scanControllerClasses();
        List<Method> requestMappingMethods = scanRequestMappingMethods(controllerClasses);
        requestMappingMethods.forEach(this::registerHandlerExecution);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> scanControllerClasses() {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);

        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> scanRequestMappingMethods(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Stream::of)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerHandlerExecution(Method method) {
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);

        RequestMethod[] methods = getTargetHttpMethods(requestMappingAnnotation);
        String url = requestMappingAnnotation.value();
        HandlerExecution handlerExecution = new HandlerExecution(method);

        registerHandlerExecution(methods, url, handlerExecution);
    }

    private RequestMethod[] getTargetHttpMethods(RequestMapping requestMappingAnnotation) {
        if (requestMappingAnnotation.method().length == 0) {
            return RequestMethod.values();
        }

        return requestMappingAnnotation.method();
    }

    private void registerHandlerExecution(RequestMethod[] methods, String url, HandlerExecution execution) {
        for (RequestMethod method : methods) {
            HandlerKey handlerKey = new HandlerKey(url, method);

            handlerExecutions.put(handlerKey, execution);
        }
    }

    public Optional<HandlerExecution> getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return Optional.ofNullable(handlerExecutions.get(handlerKey));
    }
}
