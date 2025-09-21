package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Class<?> controllerType : controllers.keySet()) {
            Set<java.lang.reflect.Method> methods = getRequestMappingMethods(controllerType);
            for (java.lang.reflect.Method method : methods) {
                if (method.getParameterCount() != 2 ||
                        !method.getParameterTypes()[0].equals(HttpServletRequest.class) ||
                        !method.getParameterTypes()[1].equals(jakarta.servlet.http.HttpServletResponse.class) ||
                        !method.getReturnType().equals(com.interface21.webmvc.servlet.ModelAndView.class)) {
                    throw new IllegalStateException("Method " + method.getName() + " in " + controllerType.getName()
                            + " has invalid signature. Expected (HttpServletRequest, HttpServletResponse) -> ModelAndView");
                }
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                addHandlerExecutions(controllers.get(controllerType), method, requestMapping);
            }
        }
    }

    private Set<java.lang.reflect.Method> getRequestMappingMethods(Class<?> controllerType) {
        return ReflectionUtils.getAllMethods(controllerType,
                ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void addHandlerExecutions(Object controller, java.lang.reflect.Method method,
                                      RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            handlerExecutions.put(new HandlerKey(requestMapping.value(), null),
                    new HandlerExecution(controller, method));
            return;
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        HandlerKey key = new HandlerKey(requestUri, requestMethod);
        Object handler = handlerExecutions.get(key);
        if (handler != null) {
            return handler;
        }
        key = new HandlerKey(requestUri, null);
        return handlerExecutions.get(key);
    }
}
