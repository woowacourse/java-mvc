package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        for (final Object basePackage : basePackage) {
            log.info("basePackage : {}", basePackage);
            final var reflections = new Reflections(basePackage);
            // 각 basePackage 위치에서 @Controller 어노테이션이 붙은 클래스 스캔
            final var controllers = reflections.getTypesAnnotatedWith(Controller.class);

            // @Controller 클래스 중 @RequestMapping이 붙은 핸들러 메서드 스캔
            for (final var controller : controllers) {
                final var controllerInstance = getInstanceBy(controller);
                log.debug("Controller: {}", controller.getName());
                final var methods = controller.getDeclaredMethods();
                for (final var method : methods) {
                    // 핸들러 메서드와 URL, HTTP Method 맵핑 정보 생성
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        putHandlerExecutions(method, controllerInstance);
                    }
                }
            }
        }
    }

    private Object getInstanceBy(final Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void putHandlerExecutions(final Method method, final Object controllerInstance) {
        final var mapping = method.getAnnotation(RequestMapping.class);
        final var value = Objects.requireNonNull(mapping).value();
        final var requestMethods = mapping.method();

        for (final var requestMethod : requestMethods) {
            final var handlerKey = new HandlerKey(value, requestMethod);
            log.debug("HandlerKey : {}", handlerKey);
            final var handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        log.debug("Target HandlerKey : {}", handlerKey);
        return handlerExecutions.get(handlerKey);
    }
}
