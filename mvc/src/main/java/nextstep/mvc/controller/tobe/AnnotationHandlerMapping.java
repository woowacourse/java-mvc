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

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            handlerExecutions.putAll(initExecutions());
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<HandlerKey, HandlerExecution> initExecutions() throws Exception {
        ControllerScanner controllerScanner = ControllerScanner.from(basePackages);
        final Map<Class<?>, Object> controllers = controllerScanner.findControllers();
        final HashMap<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Class<?> controller : controllers.keySet()) {
            handlerExecutions.putAll(createControllerExecutions(controller));
        }
        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> createControllerExecutions(final Class<?> clazz) throws Exception {
        final var constructor = clazz.getDeclaredConstructor();
        final var instance = constructor.newInstance();
        final Method[] methods = clazz.getDeclaredMethods();

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Method method : methods) {
            handlerExecutions.putAll(createMethodHandlers(instance, method));
        }
        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> createMethodHandlers(final Object instance, final Method method) {
        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = requestMapping.method();

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
        return handlerExecutions;
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}
