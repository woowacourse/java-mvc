package com.interface21.webmvc.servlet.mvc.handler;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
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
        final ControllerScanner controllerScanner = new ControllerScanner();
        final Map<Class<?>, Object> controllers = controllerScanner.scan(basePackage);
        for (final Class<?> controllerClazz : controllers.keySet()) {
            mapController(controllerClazz);
        }

        log.info("Initialized AnnotationHandlerMapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final RequestMethod method = RequestMethod.of(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(uri, method);
        return handlerExecutions.get(handlerKey);
    }

    private void mapController(final Class<?> controllerClazz) {
        try {
            final Set<Method> controllerMethods = ReflectionUtils.getAllMethods(controllerClazz,
                    ReflectionUtils.withAnnotation(RequestMapping.class));
            final Object controllerInstance = controllerClazz.getDeclaredConstructor().newInstance();
            for (final Method controllerMethod : controllerMethods) {
                mapControllerMethod(controllerMethod, controllerInstance);
            }
        } catch (final NoSuchMethodException | InvocationTargetException | InstantiationException |
                       IllegalAccessException e) {
            throw new IllegalStateException("Cannot create controller instance: " + controllerClazz, e);
        }
    }

    private void mapControllerMethod(final Method controllerMethod, final Object controllerInstance) {
        if (!controllerMethod.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        final RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
        final String uri = requestMapping.value();
        final RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            final RequestMethod[] allMethods = RequestMethod.values();
            registerHandler(allMethods, uri, controllerMethod, controllerInstance);
        } else {
            registerHandler(methods, uri, controllerMethod, controllerInstance);
        }
    }

    private void registerHandler(final RequestMethod[] methods, final String uri,
                                 final Method controllerMethod, final Object controllerInstance) {
        for (final RequestMethod method : methods) {
            final HandlerKey handlerKey = new HandlerKey(uri, method);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, controllerMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
