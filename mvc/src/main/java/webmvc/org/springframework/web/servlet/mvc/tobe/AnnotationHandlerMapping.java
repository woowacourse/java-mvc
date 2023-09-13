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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize()  {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        registerFromBasePackage(reflections);
    }

    private void registerFromBasePackage(final Reflections reflections) {
        final Set<Class<?>> clazzes = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> clazz : clazzes) {
            register(clazz);
        }
    }

    private void register(final Class<?> clazz) {
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        final List<Method> methods = Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());

        for (final Method method : methods) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            for (RequestMethod requestMethod : annotation.method()) {
                final HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
                handlerExecutions.put(handlerKey, new HandlerExecution());
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
//        log.info("requestURI = {}", request.getRequestURI());
//        log.info("method = {}", RequestMethod.valueOf(request.getMethod()));

        return handlerExecutions.get(key);
    }
}
