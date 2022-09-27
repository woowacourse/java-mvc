package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Method method : getRequestMappingMethod(controllers.keySet())) {
            addHandlerExecutions(controllers, method, method.getAnnotation(RequestMapping.class));
        }
        logInitializedRequestPath();
    }

    private Set<Method> getRequestMappingMethod(final Set<Class<?>> classes) {
        return classes.stream()
                .flatMap(clazz -> ReflectionUtils.getAllMethods(clazz).stream())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers,
                                      final Method method,
                                      final RequestMapping requestMapping) {
        HandlerExecution handlerExecution = getHandlerExecution(controllers, method);
        List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping.value(), requestMapping.method());
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.add(handlerKey, handlerExecution);
        }
    }

    private HandlerExecution getHandlerExecution(final Map<Class<?>, Object> controllers, final Method method) {
        Object controller = controllers.get(method.getDeclaringClass());
        return new HandlerExecution(controller, method);
    }

    private List<HandlerKey> getHandlerKeys(final String uri, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(uri, requestMethod))
                .collect(Collectors.toList());
    }

    private void logInitializedRequestPath() {
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.getHandlers()
                .forEach(handlerKey -> log.info("Path : {}", handlerKey));
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.getHandlerExecution(request.getRequestURI(), request.getMethod());
    }
}
