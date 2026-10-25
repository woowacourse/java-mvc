package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            for (Method handlerMethod : findHandlerMethods(entry.getKey())) {
                registerHandlerMethod(entry.getValue(), handlerMethod);
            }
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

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestUrl, RequestMethod.valueOf(requestMethod));
        return handlerExecutions.get(handlerKey);
    }
}
