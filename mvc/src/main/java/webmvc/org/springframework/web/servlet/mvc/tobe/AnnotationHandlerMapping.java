package webmvc.org.springframework.web.servlet.mvc.tobe;

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

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;
    private final HandlerKeyGenerator handlerKeyGenerator;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerKeyGenerator = new HandlerKeyGenerator(new HttpMappingExtractor());
    }

    public void initialize() {
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        final Map<Class<?>, String> uriPrefixes = controllerScanner.getUriPrefixes();
        final Set<Method> methods = toHttpMappingMethods(controllers.keySet());

        for (final Method method : methods) {
            final String prefix = uriPrefixes.get(method.getDeclaringClass());
            final Object instance = controllers.get(method.getDeclaringClass());
            final List<HandlerKey> handlerKeys = handlerKeyGenerator.generate(prefix, method);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Set<Method> toHttpMappingMethods(final Set<Class<?>> types) {
        return types.stream()
                .flatMap(type -> Arrays.stream(type.getDeclaredMethods()))
                .filter(this::isHttpMappingAnnotationPresent)
                .collect(toSet());
    }

    private boolean isHttpMappingAnnotationPresent(final Method method) {
        final boolean isHttpMappingAnnotationPresent = Arrays.stream(method.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(RequestMapping.class));
        return method.isAnnotationPresent(RequestMapping.class) || isHttpMappingAnnotationPresent;
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
