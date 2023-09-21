package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> clazz : classes) {
            initializeHandler(clazz);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeHandler(final Class<?> clazz) {
        final Object controller = makeInstance(clazz);
        final List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toUnmodifiableList());
        for (final Method method : methods) {
            initializeHandlerExecutions(method, controller);
        }
    }

    private Object makeInstance(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void initializeHandlerExecutions(final Method method, final Object controller) {
        if (!isValidHandlerExecution(method, controller)) {
            throw new IllegalArgumentException("옳지 않은 메소드이거나 없는 핸들러 입니다!");
        }
        final String uri = method.getAnnotation(RequestMapping.class).value();
        final RequestMethod[] requestMethods = method.getAnnotation(RequestMapping.class).method();
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            this.handlerExecutions.put(handlerKey, new HandlerExecution(method, controller));
        }
    }

    private boolean isValidHandlerExecution(final Method method, final Object controller) {
        return method != null && controller != null;
    }

    @Override
    public Object getHandler(final HttpServletRequest req) {
        final String uri = req.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());
        final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
