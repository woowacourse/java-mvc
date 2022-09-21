package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
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
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());

        for (Method requestMappingMethod : requestMappingMethods) {
            addHandlerExecutions(controllers, requestMappingMethod,
                    requestMappingMethod.getAnnotation(RequestMapping.class));
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> classes) {
        return classes.stream()
                .map(clazz -> ReflectionUtils.getAllMethods(clazz,
                        ReflectionUtils.withAnnotation(RequestMapping.class)))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers,
                                      final Method method,
                                      final RequestMapping requestMapping) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
            Class<?> declaringClass = requestMethod.getDeclaringClass();
            HandlerExecution handlerExecution = new HandlerExecution(controllers.get(declaringClass), method);
            handlerExecutions.put(key, handlerExecution);
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.find(request.getMethod());
        HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }
}
