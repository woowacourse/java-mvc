package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javassist.NotFoundException;
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
        try {
            initializeHandlerExecutions();
        } catch (ReflectiveOperationException e) {
            throw new InternalError("Internal error: Failed to initialize handler mapping");
        }
    }

    private void initializeHandlerExecutions() throws ReflectiveOperationException {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            initializeWithController(controllerClass);
        }
    }

    private void initializeWithController(Class<?> controllerClass) throws ReflectiveOperationException {
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            initializeWithRequestMapping(method, controllerClass.getDeclaredConstructor().newInstance());
        }
    }

    private void initializeWithRequestMapping(Method method, Object controllerClass) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
            HandlerExecution handlerExecution = new HandlerExecution(controllerClass, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) throws NotFoundException {
        HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(), RequestMethod.valueOf(request.getMethod())
        );
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new NotFoundException("Resource not found");
        }
        return handlerExecutions.get(handlerKey);
    }
}
