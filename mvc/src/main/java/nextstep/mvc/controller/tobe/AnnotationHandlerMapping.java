package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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

    @Override
    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Class<?> controller : controllers.keySet()) {
            registerHandler(controllers, controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandler(final Map<Class<?>, Object> controllers, final Class<?> controller) {
        for (Method method : controller.getMethods()) {
            initHandlerExecutions(controllers, controller, method);
        }
    }

    private void initHandlerExecutions(final Map<Class<?>, Object> controllers, final Class<?> controller,
                                       final Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod),
                    new HandlerExecution(controllers.get(controller), method));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final String method = request.getMethod();

        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.find(method)));
    }
}
