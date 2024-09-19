package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        registerHandlers(getControllers());
    }

    private Set<Class<?>> getControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerHandlers(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            for (Method method : getControllerMethods(controller)) {
                registerHandler(controller, method);
            }
        }
    }

    private List<Method> getControllerMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerHandler(Class<?> controller, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        String value = annotation.value();
        RequestMethod[] requestMethods = annotation.method();
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(value, requestMethod), handlerExecution);
        }
    }

    // TODO method 설정이 되어 있지 않으면 모든 HTTP Method를 지원한다.

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
