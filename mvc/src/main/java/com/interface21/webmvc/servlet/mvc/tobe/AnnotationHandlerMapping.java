package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        final Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
            .forEach(controllerClass -> {
                Object invoker = getInvoker(controllerClass);
                Arrays.stream(controllerClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandlerExecutions(invoker, method));
            });

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(final Object invoker, final Method method) {
        final String requestUrl = method.getAnnotation(RequestMapping.class)
            .value();
        getAvailableRequestMethods(method).forEach(requestMethod -> {
            final HandlerKey key = new HandlerKey(requestUrl, requestMethod);
            final HandlerExecution execution = HandlerExecution.from(invoker, method);

            handlerExecutions.put(key, execution);
        });
    }

    private List<RequestMethod> getAvailableRequestMethods(final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping.method().length == 0) {
            return List.of(RequestMethod.values());
        }
        return List.of(requestMapping.method());
    }

    private Object getInvoker(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey =
            new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
