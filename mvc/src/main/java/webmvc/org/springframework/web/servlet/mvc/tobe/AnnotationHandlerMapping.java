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
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerAnnotatedClass = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllerAnnotatedClass) {
            findRequestMappingAndRegisterHandler(clazz);
        }
        log.info("Initialized AnnotationHandlerMapping! {}", handlerExecutions);
    }

    private void findRequestMappingAndRegisterHandler(final Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::addHandler);
    }

    private void addHandler(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = requestMapping.method();

        Arrays.stream(requestMethods)
                .forEach(requestMethod -> handlerExecutions.put(
                        new HandlerKey(requestMapping.value(), requestMethod), new HandlerExecution(method)
                ));
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.get(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
