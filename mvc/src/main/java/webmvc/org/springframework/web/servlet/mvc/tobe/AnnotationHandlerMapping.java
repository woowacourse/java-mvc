package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
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
        for (Object subPackage : basePackage) {
            intTargetPackage(subPackage);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void intTargetPackage(final Object base) {
        final Reflections reflections = new Reflections(base);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllers) {
            initTargetController(clazz);
        }
    }

    private void initTargetController(final Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            initTargetMethod(clazz, method);
        }
    }

    private void initTargetMethod(final Class<?> clazz, final Method method) {
        if (Objects.nonNull(method.getDeclaredAnnotation(RequestMapping.class))) {
            final HandlerKey key = getHandlerKey(method);
            final Object instance = getInstance(clazz);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(key, handlerExecution);
        }
    }

    private HandlerKey getHandlerKey(final Method method) {
        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        final String uri = requestMapping.value();
        final RequestMethod requestMethod = requestMapping.method()[0];
        return new HandlerKey(uri, requestMethod);
    }

    private Object getInstance(final Class<?> clazz) {
        try {
            final Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final HandlerKey key = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(key);
    }
}
