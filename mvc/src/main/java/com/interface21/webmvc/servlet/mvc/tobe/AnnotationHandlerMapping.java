package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Arrays.stream(basePackages)
                .forEach(this::initHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutions(Object basePackage) {
        Set<Class<?>> controllerClasses = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(controllerClass -> {
            Object controller = getController(controllerClass);
            List<Method> methods = getMethods(controllerClass);
            methods.forEach(method -> addHandlerMapping(controller, method));
        });
    }

    private Object getController(Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            log.error("Cannot create controller instance: {}", controllerType.getTypeName());
            throw new RuntimeException(e);
        }
    }

    private List<Method> getMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addHandlerMapping(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();
        Arrays.stream(requestMethods)
                .forEach(requestMethod ->
                        handlerExecutions.put(
                                new HandlerKey(requestMapping.value(), requestMethod),
                                new HandlerExecution(controller, method)
                        )
                );
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, method);
        return handlerExecutions.get(key);
    }
}
