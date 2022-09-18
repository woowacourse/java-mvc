package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {

            Arrays.stream(controller.getDeclaredMethods())
                    .filter(this::filterRequestMapping)
                    .map(it -> toHandlerExecutions(controller, it))
                    .forEach(handlerExecutions::putAll);
        }
    }

    private boolean filterRequestMapping(final Method method) {
        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        return requestMapping != null;
    }

    private Map<HandlerKey, HandlerExecution> toHandlerExecutions(final Class<?> controller, final Method method) {
        try {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            final Object controllerInstance = controller.getDeclaredConstructor()
                    .newInstance();

            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            return collectHandlerExecutions(requestMapping, handlerExecution);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    private Map<HandlerKey, HandlerExecution> collectHandlerExecutions(final RequestMapping requestMapping,
                                                                       final HandlerExecution handlerExecution) {
        return Arrays.stream(requestMapping.method())
                .map(it -> new HandlerKey(requestMapping.value(), it))
                .collect(Collectors.toMap(
                        Function.identity(),
                        it -> handlerExecution
                ));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
