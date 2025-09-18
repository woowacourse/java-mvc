package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mapping.HandlerMapping;
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
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        if (contextPath == null || contextPath.isEmpty()) {
            contextPath = "";
        }

        String path = uri.substring(contextPath.length());

        if (path.isEmpty()) {
            path = "/";
        }
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(path, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerController(Class<?> clazz, Object controllerInstance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)){
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                String path = getRequestMappingValue(mapping);
                for (RequestMethod requestMethod : mapping.method()) {
                    if (mapping.method().length == 0) {
                        log.warn("method가 등록되지 않았습니다.");
                        continue;
                    }

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