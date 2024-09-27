package com.interface21.webmvc.servlet.mvc.handler.mapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.handler.HandlerKey;

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
        Map<Class<?>, Object> controllers = getControllers();
        for (Class<?> controllerClass : controllers.keySet()) {
            Set<Method> methods = findRequestMappingMethods(controllerClass);
            methods.forEach(method -> addHandlerExecution(controllers.get(controllerClass), method));
        }
    }

    private Map<Class<?>, Object> getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        controllerScanner.initialize();

        return controllerScanner.getControllers();
    }

    private Set<Method> findRequestMappingMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecution(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = getUrl(requestMapping, method.getName());
        for (RequestMethod requestMethod : getRequestMethods(requestMapping)) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(controller, method));
        }
    }

    private String getUrl(RequestMapping requestMapping, String methodName) {
        String url = requestMapping.value();
        if (url.isBlank()) {
            throw new IllegalStateException("@RequestMapping의 value값이 지정되어 있지 않습니다. methodName = " + methodName);
        }
        return url;
    }

    private Set<RequestMethod> getRequestMethods(RequestMapping requestMapping) {
        Set<RequestMethod> methods = Arrays.stream(requestMapping.method())
                .collect(Collectors.toSet());

        if (methods.isEmpty()) {
            Set<RequestMethod> allRequestMethods = Arrays.stream(RequestMethod.values())
                    .collect(Collectors.toSet());
            return allRequestMethods;
        }
        return methods;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
