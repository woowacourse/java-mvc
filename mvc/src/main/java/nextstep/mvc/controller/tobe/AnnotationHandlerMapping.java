package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
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
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }

    public void initialize() {
        log.info("Start AnnotationHandlerMapping Initialization!");
        final Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackage);

        for (final Map.Entry<Class<?>, Object> controllerEntry : controllers.entrySet()) {
            addHandlerExecutions(controllerEntry);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Map.Entry<Class<?>, Object> controllerEntry) {
        final Class<?> controllerClass = controllerEntry.getKey();
        final Object controllerInstance = controllerEntry.getValue();

        final Set<Method> methods = ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
        for (final Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
