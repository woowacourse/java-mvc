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
import java.util.*;

public class AnnotationHandlerMapping {

    private static final int EMPTY_REQUEST_METHOD_SIZE = 0;

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
        controllers.forEach(this::registerControllerMethods);

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
        RequestMethod[] requestMethods = determineRequestMethods(requestMapping);

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(method));
        }
    }

    private RequestMethod[] determineRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == EMPTY_REQUEST_METHOD_SIZE) {
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
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
