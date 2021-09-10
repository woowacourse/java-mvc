package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.ControllerScanner;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage, Controller.class);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Class<?> clazz : controllers.keySet()) {
            Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
            methodMapping(methods, controllers.get(clazz));
        }
        LOGGER.info("Initialized AnnotationHandlerMapping!");
    }

    private void methodMapping(final Set<Method> methods, final Object instance) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            requestMapping(instance, method, requestMapping);
        }
    }

    private void requestMapping(final Object instance, final Method method, final RequestMapping requestMapping) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        LOGGER.info("request info : {} , {}", request.getRequestURI(), request.getMethod());
        return handlerExecutions.get(HandlerKey.createByRequest(request));
    }
}
