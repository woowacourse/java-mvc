package com.interface21.webmvc.servlet.mvc.tobe.mapping;


import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        controllerScanner.initialize();
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::registerController);
    }

    private void registerController(Class<?> controllerClass, Object controllerInstance) {
        Set<Method> methods = findRequestMappingMethods(controllerClass);
        for (Method method : methods) {
            registerHandlerMethod(method, controllerInstance);
        }
    }

    private Set<Method> findRequestMappingMethods(Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void registerHandlerMethod(Method method, Object controllerInstance) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String path = requestMapping.value();
        for (RequestMethod httpMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(path, httpMethod);
            HandlerExecution execution = new HandlerExecution(method, controllerInstance);
            handlerExecutions.put(handlerKey, execution);
        }
    }
}
