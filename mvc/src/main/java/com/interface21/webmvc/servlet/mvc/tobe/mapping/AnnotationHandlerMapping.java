package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutions;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HandlerExecutions();
    }

    @Override
    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::registerHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecutions(Class<?> clazz, Object instance) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandlerExecution(instance, method));
    }

    private void registerHandlerExecution(Object instance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(instance, method);
        handlerExecutions.put(requestMapping, handlerExecution);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(uri, method));
    }
}
