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
        try {
            Reflections reflections = new Reflections((Object[]) basePackage);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> controllerClass : controllers) {
                Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                for (Method method : controllerClass.getDeclaredMethods()) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    if (mapping == null) {
                        continue;
                    }
                    String path = mapping.value();
                    RequestMethod[] methods = mapping.method();
                    // method 속성이 비어 있으면 모든 메서드를 지원하는 매핑으로 저장
                    if (methods.length == 0) {
                        handlerExecutions.put(new HandlerKey(path, null),
                                new HandlerExecution(controllerInstance, method));
                    } else {
                        for (RequestMethod httpMethod : methods) {
                            handlerExecutions.put(new HandlerKey(path, httpMethod),
                                    new HandlerExecution(controllerInstance, method));
                        }
                    }
                }
            }
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize AnnotationHandlerMapping", e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod reqMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(requestUri, reqMethod);
        HandlerExecution execution = handlerExecutions.get(key);
        if (execution == null) {
            execution = handlerExecutions.get(new HandlerKey(requestUri, null));
        }
        return execution;
    }
}
