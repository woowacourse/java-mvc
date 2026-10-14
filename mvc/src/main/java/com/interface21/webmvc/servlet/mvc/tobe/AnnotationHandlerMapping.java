package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        controllerScanner.scan().forEach(this::registerController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Class<?> controllerClass, final Object controller) {
        final var methods = ReflectionUtils.getAllMethods(
                controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
        for (final var method : methods) {
            registerMethod(controller, method);
        }
    }

    private void registerMethod(final Object controller, final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        final var execution = new HandlerExecution(controller, method);

        if (requestMethods.length == 0) {
            register(new HandlerKey(url, null), execution);
            return;
        }
        for (final var requestMethod : requestMethods) {
            register(new HandlerKey(url, requestMethod), execution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod =
                RequestMethod.valueOf(request.getMethod());

        // 명시적인 HTTP 메서드 매핑을 우선한다.
        final var execution = handlerExecutions.get(new HandlerKey(url, requestMethod));

        if (execution != null) {
            return execution;
        }

        // 명시적인 매핑이 없으면 메서드를 생략한 매핑을 사용한다.
        return handlerExecutions.get(new HandlerKey(url, null));
    }
    private void register(
            final HandlerKey key,
            final HandlerExecution execution
    ) {
        if (handlerExecutions.putIfAbsent(key, execution) != null) {
            throw new IllegalStateException("중복 매핑: " + key);
        }
    }
}
