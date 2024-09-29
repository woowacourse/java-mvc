package com.interface21.webmvc.servlet.mvc.tobe.mapper;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).getControllers();
        controllers.keySet().forEach(clazz -> reflect(clazz, controllers.get(clazz)));

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void reflect(Class<?> clazz, Object instance) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> addHandlerExecution(instance, method));
    }

    private void addHandlerExecution(Object handlerInstance, Method handlerMethod) {
        String uri = (String) executeMethod("value", handlerMethod);
        RequestMethod[] requestMethods = (RequestMethod[]) executeMethod("method", handlerMethod);

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        HandlerExecution handlerExecution = new HandlerExecution(handlerInstance, handlerMethod);
        Arrays.stream(requestMethods).forEach(requestMethod -> add(requestMethod, uri, handlerExecution));

        log.info("Path : {}, Method : {}", uri, requestMethods);
    }

    private Object executeMethod(String methodName, Method method) {
        try {
            Annotation annotation = method.getAnnotation(RequestMapping.class);
            return annotation.getClass()
                    .getDeclaredMethod(methodName)
                    .invoke(annotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("controller method 리플렉션 중 실패");
        }
    }

    private void add(RequestMethod requestMethod, String uri, HandlerExecution handlerExecution) {
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
