package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

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
            addAnnotatedHandlerExecution(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addAnnotatedHandlerExecution(Class<?> controller) {
        for (Method method : controller.getMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(),
                    requestMapping.method()[0]);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
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
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
