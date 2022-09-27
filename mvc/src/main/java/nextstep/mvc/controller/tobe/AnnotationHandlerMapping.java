package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.HandlerMapping;
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
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        addHandlerExecutions(controllers);
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers) {
        for (Class<?> clazz : controllers.keySet()) {
            final Method[] methods = clazz.getDeclaredMethods();
            final Object controller = controllers.get(clazz);
            checkRequestMappingMethod(controller, methods);
        }
    }

    private void checkRequestMappingMethod(final Object controller, final Method[] methods) {
        for (final Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                putHandlerExecution(controller, method);
            }
        }
    }

    private void putHandlerExecution(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] applyingMethods = requestMapping.method();
        for (final RequestMethod applyingMethod : applyingMethods) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), applyingMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));
        try {
            return handlerExecutions.get(handlerKey);
        } catch (NullPointerException e) {
            log.error("Handler Not Found", e);
            throw new IllegalStateException();
        }
    }
}
