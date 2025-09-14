package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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
        try {
            Set<Class<?>> controllerClasses = getControllersWithBasePackage();

            for (Class<?> controller : controllerClasses) {
                for (Method method : controller.getMethods()) {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    if (annotation == null) {
                        continue;
                    }

                    String value = annotation.value();
                    RequestMethod[] requestMethods = annotation.method();

                    if (requestMethods.length == 0) {
                        requestMethods = RequestMethod.values();
                    }

                    Object handler = controller.getConstructor().newInstance();
                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                        handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
                    }
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            log.error("Handler 매핑 중 오류가 발생했습니다.");
            throw new RuntimeException();
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> getControllersWithBasePackage() {
        Reflections reflections = new Reflections(this.basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        if (!handlerExecutions.containsKey(handlerKey)) {
            return null;
        }
        return handlerExecutions.get(handlerKey);
    }
}
