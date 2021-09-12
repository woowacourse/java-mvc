package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
        controllers.keySet().forEach(controllerClass -> initController(controllers, controllerClass));

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initController(Map<Class<?>, Object> controllers, Class<?> controllerClass) {
        for (Method handlerMethod : controllerClass.getDeclaredMethods()) {
            if (!handlerMethod.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            RequestMapping annotation = handlerMethod.getAnnotation(RequestMapping.class);
            Object controller = controllers.get(controllerClass);

            initHandlerMethod(handlerMethod, annotation, controller);
        }
    }

    private void initHandlerMethod(Method handlerMethod, RequestMapping annotation, Object controller) {
        handlerMethod.setAccessible(true);
        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            HandlerExecution handlerExecution = createHandlerExecution(handlerKey, handlerMethod, controller);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
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
