package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        for (Map.Entry<Class<?>, Object> entry : controllerScanner.controllers.entrySet()) {
            List<Method> methods = getRequestMethods(entry.getKey());
            addHandlers(entry.getValue(), methods);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Method> getRequestMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addHandlers(Object instance, List<Method> methods) {
        for (Method method : methods) {
            addHandler(instance, method);
        }
    }

    private void addHandler(Object instance, Method method) {
        HandlerExecution handlerExecution = (req, res) -> {
            try {
                return (ModelAndView) method.invoke(instance, req, res);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };

        List<HandlerKey> keys = getKeys(method);
        for (HandlerKey key : keys) {
            handlerExecutions.put(key, handlerExecution);
        }
    }

    private List<HandlerKey> getKeys(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping.method().length == 0) {
            return Arrays.stream(RequestMethod.values()).map(
                    requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                    .toList();
        }
        List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod requestMethod : requestMapping.method()) {
            handlerKeys.add(new HandlerKey(requestMapping.value(), requestMethod));
        }
        return handlerKeys;
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
