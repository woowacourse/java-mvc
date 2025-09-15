package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
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
        log.info("Initialized AnnotationHandlerMapping!");
        List<Class<?>> allControllerClasses = findAllControllerClasses();
        allControllerClasses.forEach(this::registerHandlerExecutionsByController);
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private List<Class<?>> findAllControllerClasses() {
        List<Class<?>> controllerClasses = new ArrayList<>();
        for (Object basePackage : basePackage) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            controllerClasses.addAll(controllers);
        }
        return controllerClasses;
    }

    private void registerHandlerExecutionsByController(Class<?> controllerClass) {
        try {
            Object controller = controllerClass.getConstructor().newInstance();
            List<Method> apiMethods = findApiMethod(controllerClass);
            apiMethods.forEach(method -> registerHandlerExecutionsByMethod(method, controller));
        } catch (NoSuchMethodException | InvocationTargetException
                 | InstantiationException | IllegalAccessException e
        ) {
            throw new IllegalArgumentException("핸들러를 등록하는 과정에서 예상치 못한 오류가 발생했습니다.");
        }
    }

    private List<Method> findApiMethod(Class<?> controllerClass) {
        return Stream.of(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerHandlerExecutionsByMethod(Method method, Object controller) {
        validateRequestMappingAnnotation(method);
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        HandlerKey handlerKey = new HandlerKey(annotation.value(), annotation.method()[0]);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private void validateRequestMappingAnnotation(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (annotation == null) {
            throw new IllegalArgumentException("RequestMapping 애노테이션이 적용되지 않은 메서드를 등록하려고 시도하고 있습니다.");
        }
        if (annotation.value() == null || annotation.value().isEmpty() || annotation.method().length == 0) {
            throw new IllegalArgumentException("RequestMapping 애노테이션의 값이 올바르지 않습니다.");
        }
    }
}
