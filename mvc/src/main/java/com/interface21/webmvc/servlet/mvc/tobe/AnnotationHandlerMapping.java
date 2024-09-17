package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        List<Object> controllers = reflections.getTypesAnnotatedWith(Controller.class).stream()
                .map(this::getControllerInstance)
                .toList();
        controllers.forEach(this::setHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object getControllerInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void setHandlerExecutions(Object controller) {
        Arrays.stream(controller.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList()
                .forEach(method -> setHandlerExecutions(controller, method));
    }

    private void setHandlerExecutions(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] methods = requestMapping.method();
        for (RequestMethod requestMethod : methods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.save(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        return handlerExecutions.find(handlerKey);
    }
}
