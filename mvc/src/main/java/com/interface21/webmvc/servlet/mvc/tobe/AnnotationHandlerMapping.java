package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.exception.NotFoundController;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");

        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        if (controllers == null) {
            throw new NotFoundController("컨트롤러가 존재하지 않습니다.");
        }
        for (Class<?> controller : controllers) {
            initializeHandlers(controller);
        }
    }

    private void initializeHandlers(final Class<?> controller)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Object controllerInstance = controller.getDeclaredConstructor().newInstance();
        final Method[] methods = controller.getDeclaredMethods();

        for (Method method : methods) {
            mapToHandler(controllerInstance, method);
        }
    }

    private void mapToHandler(final Object controllerInstance, final Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = annotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.findByName(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
