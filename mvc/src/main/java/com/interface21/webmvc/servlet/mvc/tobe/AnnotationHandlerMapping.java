package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
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

    public void initialize() {
        Set<Class<?>> controllers = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        controllers.forEach(this::addHandlerExecutionByController);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, method);

        return handlerExecutions.get(key);
    }

    private void addHandlerExecutionByController(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        Object controller = getController(clazz);
        Arrays.stream(methods).forEach(method ->
                addHandlerExecutionByMethod(controller, method));
    }

    private Object getController(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InvocationTargetException
                 | IllegalAccessException
                 | InstantiationException
                 | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void addHandlerExecutionByMethod(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        String url = requestMapping.value();
        RequestMethod[] requestMethods = getMethods(requestMapping);

        Arrays.stream(requestMethods).forEach(requestMethod -> {
            HandlerKey key = new HandlerKey(url, requestMethod);
            handlerExecutions.put(key, handlerExecution);
        });
    }

    private RequestMethod[] getMethods(RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }
}
