package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.util.ReflectionUtilsPredicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        initHandlerExecutions(basePackage);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutions(final Object[] basePackage) {
        final ControllerScanner scanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllerClasses = scanner.getControllers();

        for (final Entry<Class<?>, Object> controller : controllerClasses.entrySet()) {
            final Class<?> controllerClass = controller.getKey();
            final Object controllerInstance = controller.getValue();

            final Set<Method> methods = getMethods(controllerClass);
            addHandlerExecutions(controllerInstance, methods);
        }
    }

    private static Set<Method> getMethods(final Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass,
                ReflectionUtilsPredicates.withAnnotation(RequestMapping.class));
    }

    private void addHandlerExecutions(final Object instance, final Set<Method> methods) {
        for (final Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecution(instance, method, requestMapping);
        }
    }

    private void addHandlerExecution(final Object instance, final Method method,
                                     final RequestMapping requestMapping) {
        for (final RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(key, new HandlerExecution(instance, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        Objects.requireNonNull(request);

        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }
}
