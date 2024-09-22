package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.exception.UnsupportedMethodException;
import com.interface21.webmvc.servlet.mvc.exception.UnsupportedRequestURIException;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final int REQUEST_METHOD_ARRAY_EMPTY = 0;

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            registerControllerMethods(controller);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerControllerMethods(Class<?> controller) {
        Method[] methods = controller.getDeclaredMethods();

        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::registerHandlerMapping);
    }

    private void registerHandlerMapping(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == REQUEST_METHOD_ARRAY_EMPTY) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));

        return Optional.ofNullable(handlerExecutions.get(handlerKey))
                .orElseGet(() -> {
                    try {
                        return handleUnsupportedMethods(request, handlerKey);
                    } catch (UnsupportedMethodException | UnsupportedRequestURIException e) {
                        throw new IllegalArgumentException("오류", e);
                    }
                });
    }

    private HandlerExecution handleUnsupportedMethods(HttpServletRequest request, HandlerKey handlerKey)
            throws UnsupportedMethodException, UnsupportedRequestURIException {
        boolean hasUrl = handlerExecutions.keySet().stream()
                .anyMatch(key -> key.isSameUrl(handlerKey));

        if (hasUrl) {
            throw new UnsupportedMethodException("해당 메서드는 지원되지 않습니다: " + request.getMethod());
        }

        throw new UnsupportedRequestURIException("해당 requestURI은 지원되지 않습니다: " + request.getRequestURI());
    }
}
