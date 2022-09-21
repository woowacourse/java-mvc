package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final HandlerKey ROOT_HANDLER_KEY = new HandlerKey("/", RequestMethod.GET);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (final Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            registerHandler(entry.getKey(), entry.getValue());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandler(final Class<?> clazz, final Object controller) {
        for (Method method : clazz.getMethods()) {
            registerExecutions(controller, method);
        }
    }

    private void registerExecutions(final Object controller, final Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String requestUri = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
            log.info("Controller registered by annotation. name: {}, method: {}, uri: {}, method: {}",
                controller.getClass().getSimpleName(), method.getName(), requestUri, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
            RequestMethod.valueOf(request.getMethod()));
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        return handlerExecutions.get(ROOT_HANDLER_KEY);
    }
}
