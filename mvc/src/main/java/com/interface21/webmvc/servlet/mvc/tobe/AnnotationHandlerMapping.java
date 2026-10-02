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
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : typesAnnotatedWith) {
            Object controller;

            try {
                controller = controllerClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("컨트롤러 객체 생성에 실패했습니다.", e);
            }

            for (Method declaredMethod : controllerClass.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);

                    String value = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();
                    HandlerExecution execution = new HandlerExecution(controller, declaredMethod);

                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey key = new HandlerKey(value, requestMethod);

                        handlerExecutions.put(key, execution);
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        RequestMethod requestMethod = RequestMethod.valueOf(method);
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
