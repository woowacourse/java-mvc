package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final Reflections reflections = new Reflections(basePackage);
        for (final Class<?> controller : reflections.getTypesAnnotatedWith(Controller.class)) {
            final List<Method> methods = findRequestMappingMethods(controller);
            handlerExecutions.putAll(createHandlerExecutions(methods, controller));
        }
    }

    public List<Method> findRequestMappingMethods(final Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                     .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                     .collect(Collectors.toUnmodifiableList());
    }

    private Map<HandlerKey, HandlerExecution> createHandlerExecutions(
            final List<Method> methods,
            final Class<?> controller
    ) {
        final HashMap<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (final Method method : methods) {
            final HandlerKey handlerKey = createHandlerKey(method);
            final HandlerExecution handlerExecution = createHandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
        return Collections.unmodifiableMap(handlerExecutions);
    }

    private HandlerKey createHandlerKey(final Method method) {
        return new HandlerKey(
                method.getAnnotation(RequestMapping.class).value(),
                method.getAnnotation(RequestMapping.class).method()[0]
        );
    }

    private HandlerExecution createHandlerExecution(final Class<?> controller, final Method method) {
        try {
            final Object instance = controller.getDeclaredConstructor().newInstance();
            return new HandlerExecution(method, instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(handlerKey);
    }

    @Override
    public boolean containsHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.containsKey(handlerKey);
    }
}
