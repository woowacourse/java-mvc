package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerKey;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object basePackage : basePackages) {
            addHandlerExecution(basePackage);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecution(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            handleAddingHandlerWithMethod(controllerClass);
        }
    }

    private void handleAddingHandlerWithMethod(Class<?> controllerClass) {
        try {
            addHandlerWithMethod(controllerClass);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new HandlerReflectionException(e.getClass().getSimpleName());
        }
    }

    private void addHandlerWithMethod(Class<?> controllerClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object controller = controllerClass.getConstructor().newInstance();
        for (Method method : controllerClass.getDeclaredMethods()) {
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
            addHandlerKeyAndExecution(annotation, handlerExecution);
        }
    }

    private void addHandlerKeyAndExecution(RequestMapping annotation, HandlerExecution handlerExecution) {
        if (annotation == null) {
            return;
        }
        String url = annotation.value();
        for (RequestMethod requestMethod : annotation.method()) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
