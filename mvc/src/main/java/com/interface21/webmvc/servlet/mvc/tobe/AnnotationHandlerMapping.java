package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        try {
            final Set<Class<?>> controllers = findControllerClasses();
            initHandlerExecutions(controllers);
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (final Exception e) {
            log.error("Failed to initialize AnnotationHandlerMapping");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), method);

        return handlerExecutions.get(handlerKey);
    }

    private Set<Class<?>> findControllerClasses() {
        final Reflections reflections = new Reflections(basePackage);

        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void initHandlerExecutions(final Set<Class<?>> controllers) throws Exception {
        for (Class<?> controller : controllers) {
            initHandlerExecution(controller);
        }
    }

    private void initHandlerExecution(final Class<?> controller) throws Exception {
        final Constructor<?> constructor = ReflectionUtils.accessibleConstructor(controller);
        final Object handler = constructor.newInstance();
        final Method[] methods = controller.getDeclaredMethods();
        for (Method method : methods) {
            initHandlerExecutionByMethod(method, handler);
        }
    }

    private void initHandlerExecutionByMethod(
            final Method method,
            final Object handler
    ) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(method, handler);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
