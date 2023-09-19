package webmvc.org.springframework.web.servlet.mvc.tobe.mapping;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerKey;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
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
        initHandlerExecutions();
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutions() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            processControllerClass(aClass);
        }

    }

    private void processControllerClass(Class<?> aClass) {
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            processControllerMethod(method);
        }
    }

    private void processControllerMethod(Method method) {
        if (method.isAnnotationPresent(RequestMapping.class))
            putHandlerExecutions(method);
    }

    private void putHandlerExecutions(Method method) {
        List<HandlerKey> handlerKeys = makeHandlerKey(method);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private static List<HandlerKey> makeHandlerKey(Method method) {
        return HandlerKey.keysByAnnotation(method.getAnnotation(RequestMapping.class));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
