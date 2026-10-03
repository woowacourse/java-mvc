package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);

        controllerScanner.getControllers().forEach(this::registerHandlers);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(final Class<?> controllerClass, final Object controller) {
        String prefix = getPrefix(controllerClass);

        for (Method method : findHandlerMethods(controllerClass)) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            for (RequestMethod requestMethod : mapping.method()) {
                HandlerKey key = new HandlerKey(prefix + mapping.value(), requestMethod);
                register(key, new HandlerExecution(controller, method), method);
            }
        }
    }

    private Set<Method> findHandlerMethods(final Class<?> controllerClass) {
        return ReflectionUtils.get(ReflectionUtils.Methods.get(controllerClass)
                .filter(ReflectionUtils.withAnnotation(RequestMapping.class)));
    }

    private void register(final HandlerKey key, final HandlerExecution handlerExecution, final Method method) {
        if (handlerExecutions.putIfAbsent(key, handlerExecution) != null) {
            throw new IllegalStateException("중복된 매핑입니다: " + key + " -> " + method);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = findRequestMethod(request.getMethod());
        if (requestMethod == null) {
            return null;
        }

        String path = request.getRequestURI().substring(request.getContextPath().length());
        HandlerKey key = new HandlerKey(path, requestMethod);
        return handlerExecutions.get(key);
    }

    private RequestMethod findRequestMethod(final String method) {
        return Arrays.stream(RequestMethod.values())
                .filter(requestMethod -> requestMethod.name().equals(method))
                .findFirst()
                .orElse(null);
    }

    private String getPrefix(Class<?> controllerClass) {
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);

        if(requestMapping == null) {
            return "";
        }

        return requestMapping.value();
    }
}
