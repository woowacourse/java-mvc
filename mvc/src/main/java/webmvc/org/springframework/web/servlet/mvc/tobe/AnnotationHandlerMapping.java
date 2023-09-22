package webmvc.org.springframework.web.servlet.mvc.tobe;

import webmvc.org.springframework.web.servlet.HandlerMapping;
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
        final Reflections reflections = new Reflections(basePackage);

        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::makeHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void makeHandlerExecutions(final Class<?> controller) {
        final Set<Method> requestMethods = getRequestMethod(controller);
        final Object instance = toInstance(controller);

        for (Method method : requestMethods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);

            addHandlerExecution(handlerKeys, handlerExecution);
        }
    }

    private Set<Method> getRequestMethod(final Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private Object toInstance(Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<HandlerKey> getHandlerKeys(final RequestMapping requestMapping) {
        final String url = requestMapping.value();
        final RequestMethod[] methods = requestMapping.method();

        return Arrays.stream(methods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toList());
    }

    private void addHandlerExecution(final List<HandlerKey> handlerKeys, final HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, method);

        return handlerExecutions.get(handlerKey);
    }
}
