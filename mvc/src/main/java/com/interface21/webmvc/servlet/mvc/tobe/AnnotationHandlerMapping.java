package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public void initialize() throws Exception {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            Object controllerInstance = createControllerInstance(controller);
            List<Method> methods = findRequestMappingMethod(controller);
            mapControllerHandlers(controllerInstance, methods);
        }
    }

    private Object createControllerInstance(Class<?> controller) throws Exception {
        Constructor<?> constructor =  controller.getDeclaredConstructor();
        return constructor.newInstance();
    }

    private List<Method> findRequestMappingMethod(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void mapControllerHandlers(Object instance, List<Method> methods) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            HandlerKeys handlerKeys = new HandlerKeys(requestMapping);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            putHandlerExecutions(handlerKeys, handlerExecution);
        }
    }

    private void putHandlerExecutions(HandlerKeys handlerKeys, HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : handlerKeys.getHandlerKeys()) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
