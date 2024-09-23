package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.el.MethodNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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
    private static final String NOT_FOUND_HANDLER_NAME = "handleNotFound";

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        initialize();
    }

    public void initialize() {
        for (Class<?> controllerClass : findControllerClasses()) {
            initializeHandlerExecutions(controllerClass);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        String uri = request.getRequestURI();

        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }

        return createNotFoundHandlerExecution();
    }

    private HandlerExecution createNotFoundHandlerExecution() {
        try {
            DefaultController defaultController = DefaultController.getInstance();
            Method notFoundMethod = Arrays.stream(defaultController.getClass().getDeclaredMethods())
                    .filter(method -> method.getName().equals(NOT_FOUND_HANDLER_NAME))
                    .findAny()
                    .orElseThrow(MethodNotFoundException::new);
            return new HandlerExecution(notFoundMethod, defaultController);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Set<Class<?>> findControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void initializeHandlerExecutions(Class<?> controllerClass) {
        for (Method mapperMethod : findMapperMethods(controllerClass)) {
            addMappings(mapperMethod);
        }
    }

    private List<Method> findMapperMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addMappings(Method mapperMethod) {
        RequestMapping annotation = mapperMethod.getAnnotation(RequestMapping.class);
        Object controllerInstance = findControllerInstance(mapperMethod);

        String uri = annotation.value();
        RequestMethod[] requestMethods = annotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(mapperMethod, controllerInstance);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object findControllerInstance(Method method) {
        try {
            return method.getDeclaringClass()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("해당 method의 인스턴스를 생성할 수 없습니다.");
        }
    }
}
