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

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object obj : basePackage) {
            ControllerScanner controllerScanner = new ControllerScanner(obj);
            final Map<Class<?>, Object> controllers = controllerScanner.getController();
            controllers.forEach((controller, instance) -> {
                final Set<Method> methods = getMethodsAnnotated(controller);
                initHandlerExecutions(methods, instance);
            });
        }
    }

    private Set<Method> getMethodsAnnotated(Class<?> controller) {
        return ReflectionUtils.getAllMethods(
                controller,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );
    }

    private void initHandlerExecutions(Set<Method> methods, Object instance) {
        for (Method method : methods) {
            final HandlerKey handlerKey = getHandlerKey(method);
            final HandlerExecution handlerExecution = new HandlerExecution(method, instance);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private HandlerKey getHandlerKey(Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
    }

    public Object getHandler(HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
