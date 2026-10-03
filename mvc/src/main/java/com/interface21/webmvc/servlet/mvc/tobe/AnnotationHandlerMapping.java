package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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

        // @Controller 클래스를 찾아 객체를 만든다.
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            Object controller = createController(controllerClass);

            // @RequestMapping이 붙은 메서드를 찾는다.
            for (Method method : controllerClass.getDeclaredMethods()) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                if (mapping == null) {
                    continue;
                }
                registerHandlerExecution(method, controller, mapping);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecution(Method method, Object controller, RequestMapping mapping) {
        HandlerExecution execution = new HandlerExecution(controller, method);
        RequestMethod[] requestMethods = resolveRequestMethods(mapping);

        // URL과 HTTP 메서드를 키로 실행 정보를 등록한다.
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
            handlerExecutions.put(key, execution);
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping mapping) {
        RequestMethod[] requestMethods = mapping.method();
        // RequestMapping에 메서드가 지정되어있지 않다면 모든 메서드를 지원한다.
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }

    private Object createController(Class<?> controllerClass) {
        final Object controller;
        try {
            controller = controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + controllerClass.getName(), e);
        }
        return controller;
    }

    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        HandlerKey key = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(key);
    }
}
