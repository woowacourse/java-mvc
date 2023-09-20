package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        typesAnnotatedWith.stream()
                .map(Class::getMethods)
                .flatMap(methods -> List.of(methods).stream())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::generateHandlerMapping);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void generateHandlerMapping(final Method method) {
        final RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        for (final RequestMethod requestMethod : mapping.method()) {
            final HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(method);
            handlerExecutions.put(key, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }
}
