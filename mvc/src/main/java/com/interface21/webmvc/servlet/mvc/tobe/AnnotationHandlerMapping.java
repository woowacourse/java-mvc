package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
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
        for (final Map.Entry<Class<?>, Object> entry : controllerScanner.scan().entrySet()) {
            final Class<?> controllerClass = entry.getKey();
            final Object controller = entry.getValue();

            for (final Method method : ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class))) {
                validateHandlerMethod(method);
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                for (final RequestMethod requestMethod : resolveRequestMethods(requestMapping)) {
                    final var key = new HandlerKey(requestMapping.value(), requestMethod);
                    registerHandler(key, new HandlerExecution(controller, method));
                    log.info("Mapped {} to {}.{}", key, controllerClass.getSimpleName(), method.getName());
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var requestPath = request.getRequestURI().substring(request.getContextPath().length());
        return handlerExecutions.get(new HandlerKey(requestPath, requestMethod));
    }

    private void validateHandlerMethod(Method method) {
        final boolean validParameters = Arrays.equals(method.getParameterTypes(),
                new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class});
        if (!Modifier.isPublic(method.getModifiers())
                || !validParameters
                || !ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalStateException("Invalid handler method: "
                    + method.getDeclaringClass().getSimpleName() + "." + method.getName());
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            return RequestMethod.values();
        }

        return methods;
    }

    private void registerHandler(HandlerKey key, HandlerExecution handler) {
        if (handlerExecutions.putIfAbsent(key, handler) != null) {
            throw new IllegalStateException("Duplicate request mapping: " + key);
        }
    }
}
