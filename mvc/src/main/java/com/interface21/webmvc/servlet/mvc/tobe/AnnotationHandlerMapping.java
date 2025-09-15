package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections("samples");
        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : typesAnnotatedWithController) {
            Object controller = getController(controllerClass);
            Method[] methods = controllerClass.getMethods();

            addHandlerMappings(controller, methods);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        return handlerExecutions.get(handlerKey);
    }

    private Object getController(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void addHandlerMappings(Object controller, Method[] methods) {
        for(Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                registerRequestMapping(controller, method, requestMapping);
            }
        }
    }

    private void registerRequestMapping(Object controller, Method method, RequestMapping requestMapping) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            String value = requestMapping.value();
            handlerExecutions.put(
                    new HandlerKey(value, requestMethod),
                    new HandlerExecution(controller, method)
            );
        }
    }
}
