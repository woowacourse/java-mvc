package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
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

    public void initialize() {
        final ControllerScanner scanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = scanner.getControllers();

        for (final Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            registerRequestMappings(entry.getKey(), entry.getValue());
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }

    private void registerRequestMappings(final Class<?> controllerClass, final Object controller) {
        for (final Method method : controllerClass.getDeclaredMethods()) {
            final RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            if (mapping == null) {
                continue;
            }
            validateHandlerMethod(method);
            addMapping(method, mapping, controller);
        }
    }

    private static void validateHandlerMethod(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (!Modifier.isPublic(method.getModifiers())
                || method.getReturnType() != ModelAndView.class
                || parameterTypes.length != 2
                || parameterTypes[0] != HttpServletRequest.class
                || parameterTypes[1] != HttpServletResponse.class) {
            throw new IllegalStateException("Invalid @RequestMapping method: " + method);
        }
    }

    private void addMapping(final Method method, final RequestMapping mapping, final Object controller) {
        final String uri = mapping.value();
        final RequestMethod[] requestMethods = resolveRequestMethods(mapping);
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                throw new IllegalStateException("Duplicate request mapping: " + handlerKey);
            }
        }
    }

    private static RequestMethod[] resolveRequestMethods(final RequestMapping mapping) {
        final RequestMethod[] requestMethods = mapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
