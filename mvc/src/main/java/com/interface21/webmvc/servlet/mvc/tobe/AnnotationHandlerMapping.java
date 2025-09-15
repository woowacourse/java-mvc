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

    /**
     * @Controller 어노테이션이 붙은 클래스 중
     * @RequestMapping 어노테이션이 붙은 메서드를 찾아서 handlerExecutions을 구성하는 메서드입니다.
     */
    public void initialize() {
        try {
            Reflections reflections = new Reflections((Object[]) basePackage);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> controllerClass : controllers) {
                Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                requestMappingMethodToHandlerExecution(controllerClass, controllerInstance);
            }
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize AnnotationHandlerMapping", e);
        }
    }

    /**
     * @RequestMapping 어노테이션이 붙은 메서드를 찾아서 value와 method를 통해 HandlerExecution을 생성하고 저장하는 메서드입니다.
     */
    private void requestMappingMethodToHandlerExecution(Class<?> controllerClass, Object controllerInstance) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            if (mapping == null) {
                continue;
            }
            String path = mapping.value();
            RequestMethod[] methods = mapping.method();
            if (methods.length == 0) {
                handlerExecutions.put(new HandlerKey(path, null),
                        new HandlerExecution(controllerInstance, method));
                return;
            }
            for (RequestMethod httpMethod : methods) {
                handlerExecutions.put(new HandlerKey(path, httpMethod),
                        new HandlerExecution(controllerInstance, method));
            }
        }
    }

    /**
     * HttpServletRequest의 uri와 method를 기반으로 알맞은 HandlerExecution을 찾아서 반환하는 메서드입니다.
     */
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
