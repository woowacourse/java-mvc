package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapper;

import java.lang.reflect.Method;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class AnnotationHandlerMapper implements HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapper.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapper(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : classes) {
            initializeHandler(clazz);
        }
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet()
                .forEach(handlerKey -> log.info("Handler Key: {}", handlerKey));
    }

    private void initializeHandler(Class<?> clazz) {
        Object instance = toInstance(clazz);
        Set<Method> methods = getAnnotatedMethod(clazz);
        for (Method method : methods) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            List<HandlerKey> handlerKeys = getHandlerKeys(annotation);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            putHandlerExecution(handlerKeys, handlerExecution);
        }
    }

    private Object toInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Set<Method> getAnnotatedMethod(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(toSet());
    }

    private List<HandlerKey> getHandlerKeys(RequestMapping annotation) {
        String url = annotation.value();
        RequestMethod[] httpMethods = annotation.method();
        return Arrays.stream(httpMethods)
                .map(httpMethod -> new HandlerKey(url, httpMethod))
                .collect(toList());
    }

    private void putHandlerExecution(List<HandlerKey> handlerKeys, HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
