package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Object controller : controllers.values()) {
            registerController(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Object controller) {
        Set<Method> methods = ReflectionUtils.getAllMethods(
                controller.getClass(), ReflectionUtils.withAnnotation(RequestMapping.class));
        for (Method method : methods) {
            registerHandler(controller, method);
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        String url = mapping.value();
        RequestMethod[] methods = mapping.method().length == 0
                ? RequestMethod.values()
                : mapping.method();

        for (RequestMethod requestMethod : methods) {
            HandlerKey key = new HandlerKey(url, requestMethod);
            handlerExecutions.put(key, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(key);
    }
}
