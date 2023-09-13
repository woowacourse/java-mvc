package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import web.org.springframework.web.bind.annotation.RequestMapping;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Class<?>> controllers = getAnnotatedControllerClasses();
        for (Class<?> controller : controllers) {
            for (Method method : controller.getMethods()) {
                addHandlerExecutionWithAnnotatedMethod(method);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutionWithAnnotatedMethod(Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerKey handlerKey = new HandlerKey(requestMapping.value(),
                requestMapping.method()[0]);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private Set<Class<?>> getAnnotatedControllerClasses() {
        Set<Class<?>> controllers = new HashSet<>();
        for (Object packagePath : basePackage) {
            Reflections reflections = new Reflections((String) packagePath);
            controllers.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        }
        return controllers;
    }

    public Object getHandler(final HttpServletRequest request) {
        return null;
    }
}
