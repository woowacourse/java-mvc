package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
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
        Reflections reflections = new Reflections(basePackage);

        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : classes) {
            try {
                final Method[] methods = clazz.getDeclaredMethods();
                final Object controllerInstance = clazz.getDeclaredConstructor().newInstance();

                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                        final String value = requestMapping.value();

                        BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> targetMethod =
                                (request, response) -> {
                                    try {
                                        return (ModelAndView) method.invoke(controllerInstance, request, response);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        throw new IllegalArgumentException("Handler method invocation failed", e);
                                    }
                                };
                        for (RequestMethod requestMethod : requestMapping.method()) {
                            handlerExecutions.put(new HandlerKey(value, requestMethod),
                                    new HandlerExecution(targetMethod));
                        }
                    }
                }
            } catch (InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet()
                .forEach(path -> log.info("Path : {}, handlerExecution : {}", path,
                        handlerExecutions.get(path).getClass()));
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
