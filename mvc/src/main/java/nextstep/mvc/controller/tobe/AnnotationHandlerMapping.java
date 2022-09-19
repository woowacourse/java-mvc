package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object base : basePackage) {
            final Reflections reflections = new Reflections(base);
            final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
            final List<Method> methodsWithRequestMapping = findMethodsWithRequestMappingAnnotation(classes);
            addHandlerKeys(methodsWithRequestMapping);
        }
    }

    private List<Method> findMethodsWithRequestMappingAnnotation(final Set<Class<?>> classes) {
        return classes.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toUnmodifiableList());
    }

    private void addHandlerKeys(final List<Method> methodsWithRequestMapping) {
        for (Method method : methodsWithRequestMapping) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final String value = requestMapping.value();
            final RequestMethod[] methods = requestMapping.method();
            addHandlerKey(method, value, methods);
        }
    }

    private void addHandlerKey(final Method method, final String value, final RequestMethod[] methods) {
        for (RequestMethod requestMethod : methods) {
            handlerExecutions.put(new HandlerKey(value, requestMethod), new HandlerExecution(method));
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
