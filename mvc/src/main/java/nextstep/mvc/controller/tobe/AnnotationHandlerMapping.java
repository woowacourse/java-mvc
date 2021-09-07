package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
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

    public Object getHandler(HttpServletRequest request) {
        return null;
    }

    public void initialize() {
        log.info("Start AnnotationHandlerMapping Initialization!");
        final Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackage);

        for (final Class<?> controller : controllers.keySet()) {
            Arrays.stream(controller.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> getHandlerExecutions(controllers, controller, method));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void getHandlerExecutions(Map<Class<?>, Object> controllers, Class<?> controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
        HandlerExecution handlerExecution = new HandlerExecution(controllers.get(controller), method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }
}
