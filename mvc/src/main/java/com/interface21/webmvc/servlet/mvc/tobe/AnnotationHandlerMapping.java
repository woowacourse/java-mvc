package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 어노테이션의 요청 경로와 HTTP 메서드를 실행 대상에 매핑한다.
 * 요청 처리 전에 초기화하고, 요청 처리 중에는 조회만 한다.
 */
public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final var controllers = new ControllerScanner(basePackage).getControllers();
        handlerExecutions.clear();
        controllers.forEach(this::registerController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Class<?> controllerClass, final Object controller) {
        Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandler(controller, method));
    }

    private void registerHandler(final Object controller, final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        final var handlerExecution = new HandlerExecution(controller, method);

        Arrays.stream(getSupportedMethods(requestMapping))
                .forEach(requestMethod -> {
                    final var key = new HandlerKey(requestMapping.value(), requestMethod);
                    if (handlerExecutions.putIfAbsent(key, handlerExecution) != null) {
                        throw new IllegalStateException("중복된 요청 매핑입니다: " + key);
                    }
                });
    }

    private RequestMethod[] getSupportedMethods(final RequestMapping requestMapping) {
        final var methods = requestMapping.method();
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }

        final var key = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(key);
    }
}
