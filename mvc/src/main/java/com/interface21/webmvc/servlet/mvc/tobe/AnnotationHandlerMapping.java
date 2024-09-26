package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new ConcurrentHashMap<>();
    }

    public void initialize() {
        try {
            for (String basePackage : basePackages) {
                scanControllers(basePackage);
            }
            log.info("Initialized AnnotationHandlerMapping: {} handlers", handlerExecutions.size());
        } catch (ReflectiveOperationException e) {
            log.warn("Initialize failed: {}", e.getMessage());
        }
    }

    private void scanControllers(String basePackage) throws ReflectiveOperationException {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllers) {
            Method[] methods = controllerClass.getDeclaredMethods();
            scanControllerMethods(controllerClass, methods);
        }
    }

    private void scanControllerMethods(Class<?> controllerClass, Method[] methods) throws ReflectiveOperationException {
        for (Method method : methods) {
            registerRequestMappingMethod(controllerClass, method);
        }
    }

    private void registerRequestMappingMethod(
            Class<?> controllerClass,
            Method method
    ) throws ReflectiveOperationException {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod[] methodsArray = requestMapping.method();
            RequestMethod requestMethod = methodsArray[0];
            HandlerKey key = new HandlerKey(url, requestMethod);
            handlerExecutions.put(key, new HandlerExecution(controllerClass, method));
            log.info("Annotation Mapping Initialized: {} - {}", key, method.getName());
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey key = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(key);
    }
}
