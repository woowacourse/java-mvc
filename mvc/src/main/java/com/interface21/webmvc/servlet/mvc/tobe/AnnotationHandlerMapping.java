package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

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
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for(Class<?> controller : controllerClasses) {
            addAnnotatedHandlerExecutions(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addAnnotatedHandlerExecutions(Class<?> controller){
        List<Method> annotatedMethods = searchRequestMappingAnnotation(controller);
        annotatedMethods
                .forEach(method -> addHandlerExecutions(controller, method));
    }

    private List<Method> searchRequestMappingAnnotation(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addHandlerExecutions(Class<?> controller, Method method) {
        HandlerExecution handlerExecution = constructHandlerExecution(controller, method);
        List<HandlerKey> handlerKeys = constructHandlerKey(method);
        handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    private HandlerExecution constructHandlerExecution(Class<?> controller, Method targetMethod) {
        try {
            Constructor<?> firstConstructor = controller.getDeclaredConstructor();
            Object executionTarget = firstConstructor.newInstance();
            return new HandlerExecution(executionTarget, targetMethod);
        } catch (Exception e) {
            throw new IllegalArgumentException("메서드 타입이 다른 controller로 초기화 했습니다.");
        }
    }

    private List<HandlerKey> constructHandlerKey(Method method) {
        return HandlerKeyGenerator.fromAnnotatedMethod(method);
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(),
                RequestMethod.findMethod(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
