package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import context.org.springframework.stereotype.Controller;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        final Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        typesAnnotatedWith.stream()
                .map(Class::getMethods)
                .flatMap(methods -> List.of(methods).stream())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::generateHandlerMapping);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void generateHandlerMapping(final Method method){
        final RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        for (final RequestMethod requestMethod : mapping.method()) {
            final HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(method);
            handlerExecutions.put(key,handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }
}
