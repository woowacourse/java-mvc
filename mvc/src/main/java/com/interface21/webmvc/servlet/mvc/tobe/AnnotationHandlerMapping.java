package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    @Override
    public void initialize() {
        setHandlerExecutions(controllerScanner.getControllerClass());
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void setHandlerExecutions(Set<Class<?>> annotatedControllers) {
        annotatedControllers.forEach(this::processControllerMethods);
    }

    private void processControllerMethods(Class<?> controller) {
        for (Method method : controller.getDeclaredMethods()) {
            registerHandler(controller, method);
        }
    }

    private void registerHandler(Class<?> controller, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution execution = new HandlerExecution(controllerScanner.getControllerInstance(controller), method);

        for (RequestMethod httpMethod : mapping.method()) {
            HandlerKey key = new HandlerKey(mapping.value(), httpMethod);
            handlerExecutions.put(key, execution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.getRequestMethodBy(request.getMethod());
        HandlerKey key = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(key);
    }
}
