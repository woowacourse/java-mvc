package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.support.ControllerScanner;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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

        final Map<Class<?>, Object> instanceByClass = ControllerScanner.getControllers(basePackage);
        for (Entry<Class<?>, Object> entry : instanceByClass.entrySet()) {
            final Class<?> clazz = entry.getKey();
            final Object controller = entry.getValue();

            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(this::filterRequestMapping)
                    .map(it -> toHandlerExecutions(controller, it))
                    .forEach(handlerExecutions::putAll);
        }
    }

    private boolean filterRequestMapping(final Method method) {
        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        return requestMapping != null;
    }

    private Map<HandlerKey, HandlerExecution> toHandlerExecutions(final Object controller, final Method method) {
        try {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
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
