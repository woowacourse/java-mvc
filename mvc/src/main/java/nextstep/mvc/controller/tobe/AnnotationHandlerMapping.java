package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackage);

        for (Class<?> controllerClass : controllers.keySet()) {
            for (Method handlerMethod : controllerClass.getDeclaredMethods()) {
                if (!handlerMethod.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                RequestMapping annotation = handlerMethod.getAnnotation(RequestMapping.class);
                Object controllerInstance = controllers.get(controllerClass);
                handlerMethod.setAccessible(true);

                for (RequestMethod requestMethod : annotation.method()) {
                    HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
                    HandlerExecution handlerExecution = createHandlerExecution(handlerKey, handlerMethod, controllerInstance);
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private HandlerExecution createHandlerExecution(HandlerKey handlerKey, Method handlerMethod, Object controller) {
        HandlerExecution handlerExecution = new HandlerExecution(handlerMethod, controller);
        handlerExecutions.put(handlerKey, handlerExecution);
        return handlerExecution;
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));

        return handlerExecutions.get(handlerKey);
    }
}
