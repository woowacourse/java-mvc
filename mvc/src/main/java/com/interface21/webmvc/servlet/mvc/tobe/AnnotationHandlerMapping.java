package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            handlerExecutions.putAll(findHandlerExecutions());
            log.info("Initialized AnnotationHandlerMapping");
        } catch (Exception e) {
            log.error("error occurred while initializing AnnotationHandlerMapping");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions
                .get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    private Map<HandlerKey, HandlerExecution> findHandlerExecutions() throws Exception {
        // basePackages에서 Controller 어노테이션이 붙은 클래스 찾기
        Set<Class<?>> controllerClasses = new Reflections(basePackages)
                        .getTypesAnnotatedWith(Controller.class);

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            // controllerClass에 대한 handlerExecutions 찾기
            handlerExecutions.putAll(findHandlerMethods(controllerClass));
        }
        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> findHandlerMethods(final Class<?> controllerClass) throws Exception {
        // controller 객체 생성
        var controller = controllerClass.getDeclaredConstructor().newInstance();
        // controller에 정의된 메서드 목록 조회
        Method[] methods = controllerClass.getDeclaredMethods();

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Method method : methods) {
            // RequestMapping 어노테이션이 붙은 메서드 조회
            Annotation annotation = method.getDeclaredAnnotation(RequestMapping.class);
            if (annotation != null) {
                handlerExecutions.putAll(createHandlerMapping(controller, method));
            }
        }
        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> createHandlerMapping(final Object controller, final Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        // RequestMethod(HandlerKey)에 따른 HandlerExecution 매핑
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }

        return handlerExecutions;
    }
}
