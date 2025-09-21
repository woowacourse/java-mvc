package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map.Entry;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackages);
    }

    // 초기화 메서드
    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers(); // 컨트롤러와 인스턴스 쌍

        for (Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> controllerClass = entry.getKey();
            Object controllerInstance = entry.getValue();
            registerHandlerMethods(controllerClass, controllerInstance);
        }
    }

    // 핸들러 메서드들 등록
    private void registerHandlerMethods(final Class<?> controllerClass, final Object controllerInstance) {
        Set<Method> methods = ReflectionUtils.getAllMethods(
                controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class),
                ReflectionUtils.withModifier(Modifier.PUBLIC)
        );

        for (Method method : methods) {
            registerHandlerMethod(controllerInstance, method);
        }
    }

    // 핸들러 메서드 등록
    private void registerHandlerMethod(final Object controllerInstance, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values(); // 모든 메서드 허용
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

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
    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
