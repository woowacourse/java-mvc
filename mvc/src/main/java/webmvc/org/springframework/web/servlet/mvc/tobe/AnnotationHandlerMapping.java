package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerAnnotatedClass = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllerAnnotatedClass) {
            findRequestMappingAndRegisterHandler(clazz);
        }
        log.info("Initialized AnnotationHandlerMapping! {}", handlerExecutions);
    }

    @Override
    public boolean support(final Request request) {
        return handlerExecutions.containsKey(new HandlerKey(request.getUri(), request.getMethod()));
    }

    private void findRequestMappingAndRegisterHandler(final Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> addHandler(method, clazz));
    }

    private void addHandler(final Method method, final Class<?> clazz) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = requestMapping.method();

        Arrays.stream(requestMethods)
                .forEach(requestMethod -> handlerExecutions.put(
                        createHandlerKey(requestMapping, requestMethod), createHandlerExecution(method, clazz)
                ));
    }

    private HandlerKey createHandlerKey(final RequestMapping requestMapping, final RequestMethod requestMethod) {
        return new HandlerKey(requestMapping.value(), requestMethod);
    }

    private HandlerExecution createHandlerExecution(final Method method, final Class<?> clazz) {
        try {
            return new HandlerExecution(method, clazz.getDeclaredConstructor().newInstance());
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e
        ) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getHandler(final Request request) {
        final HandlerKey handlerKey = new HandlerKey(request.getUri(), request.getMethod());
        return handlerExecutions.get(handlerKey);
    }
}
