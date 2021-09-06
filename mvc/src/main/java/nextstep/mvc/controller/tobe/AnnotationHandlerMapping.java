package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        mapControllers(controllers);
        LOG.info("Initialized AnnotationHandlerMapping!");
    }

    private void mapControllers(Map<Class<?>, Object> controllers) {
        for (Class<?> controllerClass : controllers.keySet()) {
            Set<Method> methods = ReflectionUtils.getAllMethods(controllerClass,
                    ReflectionUtils.withAnnotation(RequestMapping.class));
            mapMethods(controllers, controllerClass, methods);
        }
    }

    private void mapMethods(Map<Class<?>, Object> controllers, Class<?> controllerClass, Set<Method> methods) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
            HandlerExecution handlerExecution = new HandlerExecution(controllers.get(controllerClass), method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey =
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
