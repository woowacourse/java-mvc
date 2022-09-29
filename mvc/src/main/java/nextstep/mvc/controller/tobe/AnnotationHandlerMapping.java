package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        final Set<Method> mappingMethods = getRequestMappingMethods(controllers.keySet());

        for (Method method : mappingMethods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecution(controllers, method, requestMapping);
        }
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        final Set<Method> methods = new HashSet<>();
        for (Class<?> clazz : controllers) {
            methods.addAll(Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toList()));
        }
        return methods;
    }

    private void addHandlerExecution(final Map<Class<?>, Object> controllers, final Method method,
                                     final RequestMapping requestMapping) {
        if (requestMapping != null) {
            final List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
            final Object handler = controllers.get(method.getDeclaringClass());

            for (HandlerKey handlerKey : handlerKeys) {
                handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
            }
        }
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();

        for (RequestMethod requestMethod : requestMethods) {
            handlerKeys.add(new HandlerKey(url, requestMethod));
        }
        return handlerKeys;
    }
}
