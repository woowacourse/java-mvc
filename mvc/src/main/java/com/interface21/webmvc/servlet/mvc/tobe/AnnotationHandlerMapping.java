package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final Map<Class<?>, Object> controllers = ControllerScanner.scan(basePackage);

        for (final Entry<Class<?>, Object> entry : controllers.entrySet()) {
            registerHandlerMethods(entry.getKey(), entry.getValue());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerMethods(final Class<?> clazz, final Object controller) {
        final Set<Method> allMethods = ReflectionUtils.getAllMethods(
                clazz,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );

        for (final Method method : allMethods) {
            registerHandlerMethod(method, controller);
        }
    }

    private void registerHandlerMethod(final Method method, final Object controller) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        registerMapping(controller, method, url, requestMethods);
    }

    private void registerMapping(
            final Object controller,
            final Method method,
            final String url,
            final RequestMethod[] requestMethods
    ) {
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String httpMethod = request.getMethod();

        final RequestMethod requestMethod = RequestMethod.getBy(httpMethod);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
