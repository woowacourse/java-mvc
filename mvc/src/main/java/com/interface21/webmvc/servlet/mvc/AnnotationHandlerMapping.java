package com.interface21.webmvc.servlet.mvc;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.controllerScanner = new ControllerScanner(basePackages);
    }

    @Override
    public void initialize() {
        final var controllers = controllerScanner.scan();

        for (final var entry : controllers.entrySet()) {
            final var controllerClass = entry.getKey();
            final var controllerInstance = entry.getValue();

            for (final var method : controllerClass.getDeclaredMethods()) {
                registerHandlerMethods(controllerInstance, method);
            }
        }
    }

    private void registerHandlerMethods(final Object controller, final Method method) {
        if (!isHandlerMethodCandidate(method)) {
            return;
        }

        final var mapping = method.getAnnotation(RequestMapping.class);
        final var url = mapping.value();
        final var requestMethods = mapping.method().length > 0 ? mapping.method() : RequestMethod.values();

        for (final var requestMethod : requestMethods) {
            registerHandler(url, requestMethod, controller, method);
        }
    }

    private boolean isHandlerMethodCandidate(final Method method) {
        return method.isAnnotationPresent(RequestMapping.class)
                && Modifier.isPublic(method.getModifiers())
                && isValidHandlerMethod(method);
    }

    private boolean isValidHandlerMethod(final Method method) {
        final var params = method.getParameterTypes();
        if (params.length != 2 || params[0] != HttpServletRequest.class || params[1] != HttpServletResponse.class) {
            return false;
        }

        return method.getReturnType() == ModelAndView.class;
    }

    private void registerHandler(
            final String url,
            final RequestMethod requestMethod,
            final Object controller,
            final Method method
    ) {
        final var handlerKey = new HandlerKey(url, requestMethod);
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("Duplicate mapping: " + requestMethod + " " + url);
        }

        handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
    }

    public Object getHandler(final HttpServletRequest request) {
        final var uri = request.getRequestURI();
        final var methodName = request.getMethod();

        final var method = RequestMethod.from(methodName)
                .orElseThrow(() -> new IllegalStateException("Unsupported HTTP method: " + methodName + " for " + uri));

        return handlerExecutions.get(new HandlerKey(uri, method));
    }
}
