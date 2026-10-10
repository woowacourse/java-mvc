package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
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
        for (Class<?> controllerClass : scanControllerClasses()) {
            Object controller = createController(controllerClass);
            for (Method handlerMethod : findHandlerMethods(controllerClass)) {
                registerHandlerMethod(controller, handlerMethod);
            }
        }
    }

    private Set<Class<?>> scanControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Object createController(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("리플렉션 작업 중 예외 발생: " + controllerClass.getName(), e);
        }
    }

    private List<Method> findHandlerMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerHandlerMethod(Object controller, Method handlerMethod) {
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        HandlerExecution handlerExecution = new HandlerExecution(handlerMethod, controller);
        for (RequestMethod requestMethod : resolveRequestMethods(requestMapping)) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), handlerExecution);
            log.info("{} {} -> {}", requestMethod, url, handlerMethod);
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestUrl, RequestMethod.valueOf(requestMethod));
        return handlerExecutions.get(handlerKey);
    }
}
