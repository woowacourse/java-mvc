package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final Map<Class<?>, Object> controllers = controllerScanner.getHandlerExecutions();
        for (final var entry : controllers.entrySet()) {
            final var clazz = entry.getKey();
            final var controller = entry.getValue();
            appendHandlerExecutionPerController(clazz, controller);
        }
    }

    private void appendHandlerExecutionPerController(final Class<?> clazz, final Object controller) {
        final var methods = clazz.getMethods();
        for (final var method : methods) {
            appendHandlerExecutionPerMethod(controller, method);
        }
    }

    private void appendHandlerExecutionPerMethod(final Object controller, final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            appendHandlerExecutionPerRequest(requestMapping, handlerExecution);
        }
    }

    private void appendHandlerExecutionPerRequest(final RequestMapping requestMapping,
                                                  final HandlerExecution handlerExecution) {
        final List<HandlerKey> handlerKeys = mapToHandlerKeys(requestMapping);
        for (final var handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<HandlerKey> mapToHandlerKeys(final RequestMapping requestMapping) {
        final String requestUrl = requestMapping.value();
        final List<RequestMethod> requestMethods = List.of(requestMapping.method());

        return requestMethods.stream()
                .map(requestMethod -> new HandlerKey(requestUrl, requestMethod))
                .collect(Collectors.toUnmodifiableList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
