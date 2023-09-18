package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.util.ReflectionUtils;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : annotatedControllers) {
            instantiateAndStore(clazz);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void instantiateAndStore(Class<?> clazz) {
        Object handler = ReflectionUtils.instantiate(clazz);
        storeAnnotatedHandlers(clazz, handler);
    }

    private void storeAnnotatedHandlers(Class<?> clazz, Object handler) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                storeHandler(handler, method);
            }
        }
    }

    private void storeHandler(Object handler, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(
                RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        HandlerMethod handlerMethod = new HandlerMethod(handler, method);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handlerMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(),
                        RequestMethod.resolve(request.getMethod())));
    }
}
