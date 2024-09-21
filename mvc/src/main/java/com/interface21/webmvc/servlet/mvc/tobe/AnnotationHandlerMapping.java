package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            processControllers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void processControllers() throws Exception {
        for (Class<?> controller : findControllers()) {
            processController(controller);
        }
    }

    private Set<Class<?>> findControllers() {
        Reflections reflections = new Reflections(basePackage);

        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void processController(Class<?> controller) throws Exception {
        Object runnerInstance = controller.getDeclaredConstructor().newInstance();

        for (Method method : findHandlerMethods(controller)) {
            appendRequestMapping(method, runnerInstance);
        }
    }

    private List<Method> findHandlerMethods(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void appendRequestMapping(Method method, Object runnerInstance) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(runnerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    protected Object getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
