package com.interface21.webmvc.servlet.handler.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            ControllerScanner controllerScanner = ControllerScanner.from(basePackages);
            handlerExecutions.putAll(findHandlerExecutions(controllerScanner));
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

    private Map<HandlerKey, HandlerExecution> findHandlerExecutions(ControllerScanner controllerScanner)
            throws Exception {
        Map<Class<?>, Object> controllerClasses = controllerScanner.getControllers();

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Map.Entry<Class<?>, Object> entry : controllerClasses.entrySet()) {
            // controllerClass에 대한 handlerExecutions 찾기
            handlerExecutions.putAll(findHandlerMethods(entry.getKey(), entry.getValue()));
        }

        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> findHandlerMethods(
            final Class<?> controllerClass,
            final Object controller
    ) {
        // getMethods(): public 메서드만 가져옴, 상속된 메서드도 포함
        // getDeclaredMethods(): 클래스 코드 안에 선언된 모든 메서드를 접근제어자 상관없이 가져옴
        // ReflectionUtils.getAllMethods(): 선언된 메서드 + 상속된 메서드를 접근제어자 상관없이 가져옴(오버라이드한 경우, 부모/자식 메서드 둘 다 가져옴)
        // Spring MVC의 @RequestMapping은 접근제어자와는 무관하게 인식함
        Set<Method> methods = ReflectionUtils.getAllMethods(
                controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Method method : methods) {
            // RequestMapping 어노테이션이 붙은 메서드 조회
            Annotation annotation = method.getDeclaredAnnotation(RequestMapping.class);
            if (annotation != null) {
                handlerExecutions.putAll(createHandlerExecutions(controller, method));
            }
        }
        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> createHandlerExecutions(final Object controller, final Method method) {
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
