package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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

    // 여기에서 초기화하면서 handlerExceptions 채우기
    public void initialize() {
        // @Controller 가 달린 클래스 찾기
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllers = findAnnotatedControllers(reflections);

        setHandlerExecutions(annotatedControllers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> findAnnotatedControllers(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void setHandlerExecutions(Set<Class<?>> annotatedControllers) {
        annotatedControllers.forEach(this::processControllerMethods);
    }

    private void processControllerMethods(Class<?> controller) {
        for (Method method : controller.getDeclaredMethods()) {
            registerHandler(controller, method);
        }
    }
    
    private void registerHandler(Class<?> controller, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution execution = new HandlerExecution(controller, method);

        for (RequestMethod httpMethod : mapping.method()) {
            HandlerKey key = new HandlerKey(mapping.value(), httpMethod);
            handlerExecutions.put(key, execution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        // 여기서 HandlerExecution 이 나가야함.
        // 하는 것 = request를 받아서 handlerExecution 찾아서 반환.
        // key는 handler key
        RequestMethod requestMethod = RequestMethod.getRequestMethodBy(request.getMethod());
        HandlerKey key = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(key);
    }
}
