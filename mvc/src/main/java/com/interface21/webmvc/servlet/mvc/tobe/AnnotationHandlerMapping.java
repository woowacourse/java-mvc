package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class AnnotationHandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllerInstance = controllerScanner.getControllerInstance();

        List<Class<?>> controllers = controllerScanner.getControllers();
        List<Method> controllerMethods = extractControllerMethods(controllers);
        for (Method controllerMethod : controllerMethods) {
            HandlerExecution handlerExecution = new HandlerExecution(controllerMethod);

            RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
            List<HandlerKey> handlerKeys = extractHandlerKeys(requestMapping);

            for (HandlerKey handlerKey : handlerKeys) {
                this.handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private List<Method> extractControllerMethods(List<Class<?>> controllers) {
        return controllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private List<HandlerKey> extractHandlerKeys(RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            handlerKeys.add(new HandlerKey(requestMapping.value(), requestMethod));
        }

        return handlerKeys;
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return this.handlerExecutions.get(handlerKey);
    }
}
