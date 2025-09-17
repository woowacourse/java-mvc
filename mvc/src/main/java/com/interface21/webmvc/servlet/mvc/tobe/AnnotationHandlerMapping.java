package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        log.info("Initialized AnnotationHandlerMapping!");
        final var controllers = scanControllers();
        for (final Class<?> controller : controllers) {
            final var methods = scanRequestMappingMethods(controller);
            registerHandlerForController(controller, methods);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var method = request.getMethod();
        final var handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }

    private Set<Class<?>> scanControllers() {
        return new Reflections(basePackage)
                .getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> scanRequestMappingMethods(final Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerHandlerForController(final Class<?> controller, final List<Method> methods) {
        for (final Method method : methods) {
            final var handlerKeys = createHandlerKeys(method);
            final var handlerExecution = createHandlerExecution(controller, method);
            registerHandler(handlerKeys, handlerExecution);
        }
    }

    private List<HandlerKey> createHandlerKeys(final Method method) {
        final var requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        var url = requestMappingAnnotation.value();
        var httpMethods = requestMappingAnnotation.method();

        if (httpMethods.length == 0) {
            return Arrays.stream(RequestMethod.values())
                    .map(requestMethod -> new HandlerKey(url, requestMethod))
                    .toList();
        }
        return Arrays.stream(httpMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }

    private HandlerExecution createHandlerExecution(final Class<?> controller, final Method method) {
        try {
            final Constructor<?> constructor = controller.getDeclaredConstructor();
            return new HandlerExecution(constructor.newInstance(), method);
        } catch (NoSuchMethodException e) {
            log.error("Exception : 컨트롤러 기본 생성자가 필요합니다. | {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void registerHandler(final List<HandlerKey> handlerKeys, final HandlerExecution handlerExecution) {
        for (final HandlerKey handlerKey : handlerKeys) {
            registerHandler(handlerKey, handlerExecution);
        }
    }

    private void registerHandler(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            log.warn("Handler Key : {} 가 이미 등록되어 있습니다. | {}", handlerKey, handlerExecutions.get(handlerKey));
            throw new IllegalArgumentException();
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }
}
