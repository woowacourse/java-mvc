package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackages);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        final Set<Method> methods = getRequestMappingMethods(controllers.keySet());
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> classes) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> clazz : classes) {
            methods.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return methods;
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers, final Method method,
                                      final RequestMapping requestMapping) {
        final Object controller = controllers.get(method.getDeclaringClass());
        final List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    private List<HandlerKey> mapHandlerKeys(final String url, final RequestMethod[] methods) {
        return Stream.of(methods)
                .map(method -> new HandlerKey(url, method))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
