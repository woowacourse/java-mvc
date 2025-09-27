package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.execution.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        requireBasePackages(basePackage);

        try {
            final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
            Map<Class<?>, Object> controllers = controllerScanner.getControllers();
            registerControllers(controllers);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void requireBasePackages(final Object[] basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            throw new IllegalArgumentException("backPackage가 설정되어야 합니다");
        }
    }

    private void registerControllers(final Map<Class<?>, Object> controllers)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (controllers.isEmpty()) {
            log.warn("basePackge 내에 컨트롤러를 찾을 수 없습니다");
        }

        for (Class<?> controller : controllers.keySet()) {
            final Object handler = controllers.get(controller);
            final Method[] methods = controller.getDeclaredMethods();

            scanHandlerMethods(methods, handler);
        }
    }

    private void scanHandlerMethods(final Method[] methods, final Object handler) {
        for (Method method : methods) {
            registerRequestMapping(handler, method);
        }
    }

    private void registerRequestMapping(final Object handler, final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final List<HandlerKey> handlerKeys = createHandlerKeys(requestMapping);

            for (HandlerKey handlerKey : handlerKeys) {
                final HandlerExecution handlerExecution = new HandlerExecution(handler, method);

                registerHandler(handlerKey, handlerExecution);
            }
        }
    }

    private List<HandlerKey> createHandlerKeys(final RequestMapping requestMapping) {
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();

        final RequestMethod[] targetMethods = getRequestMethods(requestMethods);

        return Arrays.stream(targetMethods)
                .map(targetMethod -> new HandlerKey(url, targetMethod))
                .toList();
    }

    private void registerHandler(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("같은 RequestMapping url에 대한 중복 매핑이 불가능합니다");
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private RequestMethod[] getRequestMethods(final RequestMethod[] requestMethods) {
        RequestMethod[] targetMethods = requestMethods;

        if (requestMethods.length == 0) {
            targetMethods = RequestMethod.values();
        }
        return targetMethods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        final RequestMethod requestMethod = RequestMethod.from(method);

        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
