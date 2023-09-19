package webmvc.org.springframework.web.servlet.mvc.tobe;

import static java.util.Arrays.stream;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, Method> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        new Reflections(basePackage)
                .getTypesAnnotatedWith(Controller.class)
                .stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .flatMap(declaredMethod -> stream(declaredMethod.getDeclaredAnnotations())
                        .filter(it -> it.annotationType().equals(RequestMapping.class))
                        .map(RequestMapping.class::cast)
                        .flatMap(declaredAnnotation -> stream(declaredAnnotation.method())
                                .map(it -> Map.entry(
                                        new HandlerKey(declaredAnnotation.value(), it),
                                        declaredMethod))
                        )
                )
                .forEach(it -> handlerExecutions.put(it.getKey(), it.getValue()));
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
