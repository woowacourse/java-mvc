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

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            Method[] declaredMethods = controllerClass.getDeclaredMethods();
            extracted(controllerClass, declaredMethods);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void extracted(Class<?> controllerClass, Method[] declaredMethods) {
        for (Method declaredMethod : declaredMethods) {
            RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            putHandlerExecution(controllerClass, declaredMethod, annotation);
        }
    }

    private void putHandlerExecution(Class<?> controllerClass, Method declaredMethod, RequestMapping annotation) {
        if (annotation != null) { // todo: RequestMapping method없을 시 모두 매핑하도록
            HandlerKey handlerKey = new HandlerKey(annotation.value(), annotation.method()[0]);
            HandlerExecution handlerExecution = new HandlerExecution(controllerClass, declaredMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
