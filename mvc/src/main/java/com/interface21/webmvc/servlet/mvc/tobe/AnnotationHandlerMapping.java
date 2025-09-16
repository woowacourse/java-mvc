package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import com.interface21.webmvc.servlet.mvc.asis.ControllerScanner;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(new Reflections(basePackage));
    }

    @Override
    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::registerController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());

        if (requestPath.isBlank() || !requestPath.startsWith("/")) {
            requestPath = "/" + requestPath;
        }
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestPath, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerController(Class<?> clazz, Object controllerInstance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)){
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                String path = getRequestMappingValue(mapping);
                for (RequestMethod requestMethod : mapping.method()) {
                    handlerExecutions.put(
                            new HandlerKey(path, requestMethod),
                            new HandlerExecution(controllerInstance, method)
                    );
                }
            }
        }
    }

    private static String getRequestMappingValue(RequestMapping mapping) {
        String path = mapping.value();
        if (path.isEmpty()) {
            path = "/";
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }
}