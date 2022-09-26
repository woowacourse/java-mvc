package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var scanner = new ControllerScanner(this.basePackage);
        mapControllers(scanner.getControllers());
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void mapControllers(Map<Class<?>, Object> controllers) {
        for (final var clazz : controllers.keySet()) {
            final var controller = controllers.get(clazz);
            mapHandlers(clazz, controller);
        }
    }

    private void mapHandlers(Class<?> clazz, Object controller) {
        for (final var method : clazz.getMethods()) {
            mapHandler(controller, method);
        }
    }

    private void mapHandler(Object controller, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        for (final var httpMethod : requestMapping.method()) {
            final var key = new HandlerKey(requestMapping.value(), httpMethod);
            handlerExecutions.put(key, new HandlerExecution(controller, method));
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final var url = request.getRequestURI();
        final var requestMethod = RequestMethod.find(request.getMethod());
        final var key = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(key);
    }
}
