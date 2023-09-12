package webmvc.org.springframework.web.servlet.mvc.tobe;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

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
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        final Set<Method> methods = getRequestMappingMethod(controllers.keySet());

        for (final Method method : methods) {
            final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            final List<HandlerKey> handlerKeys = mapHandlerKeys(annotation.value(), annotation.method());
            final Object instance = controllers.get(method.getDeclaringClass());
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }
    }

    public Set<Method> getRequestMappingMethod(final Set<Class<?>> types) {
        return types.stream()
                .flatMap(type -> Arrays.stream(type.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(toSet());
    }

    public List<HandlerKey> mapHandlerKeys(final String value, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(value, requestMethod))
                .collect(toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
