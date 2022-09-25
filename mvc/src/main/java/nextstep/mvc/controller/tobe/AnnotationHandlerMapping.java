package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner();
        Map<Class<?>, Object> controllers = controllerScanner.getControllers(basePackage);

        Set<Method> methods = getRequestMappingMethods(controllers.keySet());
        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllers) {
        Set<Method> requestMappingMethods = new HashSet<>();
        for (Class<?> controller : controllers) {
            Set<Method> methods = ReflectionUtils.getAllMethods(
                    controller, ReflectionUtils.withAnnotation(RequestMapping.class));
            requestMappingMethods.addAll(methods);
        }
        return requestMappingMethods;
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers, final Method method,
                                      final RequestMapping requestMapping) {
        Class<?> controller = method.getDeclaringClass();
        Object handler = controllers.get(controller);

        HandlerExecution handlerExecution = new HandlerExecution(handler, method);
        List<HandlerKey> handlerKeys = mapHandlerKey(requestMapping.value(), requestMapping.method());
        for (HandlerKey handlerKey : handlerKeys) {
            this.handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<HandlerKey> mapHandlerKey(final String url, final RequestMethod[] methods) {
        return Stream.of(methods)
                .map(method -> new HandlerKey(url, method))
                .collect(Collectors.toList());
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(url, method);

        return Optional.ofNullable(handlerExecutions.get(handlerKey));
    }
}
