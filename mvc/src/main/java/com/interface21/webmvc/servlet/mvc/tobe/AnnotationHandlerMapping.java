package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers =  reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(this::registerController);
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerController(Class<?> controller) {
        Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerMappings(controller, method));
    }

    private void registerMappings(Class<?> controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        List<RequestMethod> requestMethods = Arrays.stream(requestMapping.method()).toList();
        if (requestMethods.isEmpty()) {
            requestMethods = resolveRequestMethods();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            registerUniqueHandler(handlerKey, handlerExecution);
        }
    }

    private List<RequestMethod> resolveRequestMethods() {
        return Arrays.stream(RequestMethod.values()).toList();
    }

    private void registerUniqueHandler(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("중복된 URL과 HTTP 메서드가 등록될 수 없습니다: " + handlerKey.toString());
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }
}
