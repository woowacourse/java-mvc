package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
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
        log.info("Initialized AnnotationHandlerMapping!");
        putHandlerExecutions();
    }

    private void putHandlerExecutions() {
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::processControllerClass);
    }

    private void processControllerClass(Class<?> aClass) {
        Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::processControllerMethod);
    }

    private void processControllerMethod(Method method) {
        HandlerKey handlerKey = makeHandlerKey(method);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private static HandlerKey makeHandlerKey(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String url = annotation.value();
        RequestMethod requestMethod = annotation.method()[0];
        return new HandlerKey(url, requestMethod);
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
