package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var reflections = new Reflections(basePackage);
        final var methods = findAllControllerMethods(reflections);
        collectMethodsToHandlerExecutions(methods);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void collectMethodsToHandlerExecutions(final List<Method> methods) {
        for (Method method : methods) {
            final var requestMapping = method.getAnnotation(RequestMapping.class);
            final var handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);

            handlerExecutions.put(handlerKey, new HandlerExecution(method));
        }
    }

    private List<Method> findAllControllerMethods(final Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Stream::of)
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final var uri = request.getRequestURI();
        final var method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(uri, RequestMethod.from(method));

        return handlerExecutions.get(handlerKey);
    }
}
