package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map.Entry;
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
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackages);
    }

    // 초기화 메서드
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers(); // 컨트롤러와 인스턴스 쌍

        for (Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> controllerClass = entry.getKey();
            Object controllerInstance = entry.getValue();
            detectAndRegisterHandlerMethods(controllerClass, controllerInstance);
        }
    }

    // 컨트롤러 클래스에서 핸들러 메서드 탐색 및 등록
    private void detectAndRegisterHandlerMethods(final Class<?> controllerClass, final Object controllerInstance) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (isHandlerMethod(method)) {
                registerHandlerMethod(controllerInstance, method);
            }
        }
    }

    // 핸들러 메서드 여부 확인
    private boolean isHandlerMethod(final Method method) {
        return method.isAnnotationPresent(RequestMapping.class) && Modifier.isPublic(method.getModifiers());
    }

    // 핸들러 메서드 등록
    private void registerHandlerMethod(final Object controller, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values(); // 모든 메서드 허용
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);

            validateDuplicateHandlerKey(handlerKey, handlerExecution);
            log.debug("Handler registered: {} {}", requestMethod, uri);
        }
    }

    // 중복 핸들러 키 검사
    private void validateDuplicateHandlerKey(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        if(handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("Duplicate mapping detected: " + handlerKey);
        }

        handlerExecutions.put(handlerKey, handlerExecution);
    }

    // 요청으로부터 핸들러 조회
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

