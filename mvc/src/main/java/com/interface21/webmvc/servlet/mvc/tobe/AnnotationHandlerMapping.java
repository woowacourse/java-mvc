package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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

        final Reflections reflections = new Reflections(basePackage);
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
            try {
                registerHandlers(clazz);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Can't create controller: " + clazz.getName(), e);
            }
        }
    }

    private void registerHandlers(Class<?> clazz) throws ReflectiveOperationException {
        Object controller = ReflectionUtils.accessibleConstructor(clazz).newInstance();
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            registerHandlerMethod(method, controller);
        }

    }

    private void registerHandlerMethod(Method method, Object controller) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        ReflectionUtils.makeAccessible(method);
        String value = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalArgumentException("Duplicated path and method.");
            }
            handlerExecutions.put(
                    handlerKey,
                    new HandlerExecution(controller, method)
            );
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.valueOf(method)));
    }
}
