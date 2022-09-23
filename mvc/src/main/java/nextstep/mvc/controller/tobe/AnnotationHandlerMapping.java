package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
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
        log.info("Initialized AnnotationHandlerMapping!");

        final ControllerScanner controllerScanner = new ControllerScanner(basePackages);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (final Entry<Class<?>, Object> controller : controllers.entrySet()) {
            addHandlerExecution(controller);
        }
    }

    private void addHandlerExecution(final Entry<Class<?>, Object> controller) {
        final Class<?> controllerClass = controller.getKey();
        final Object controllerInstance = controller.getValue();
        final Set<Method> methods = getRequestMappingMethods(controllerClass);

        for (final Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final RequestMethod[] requestMethods = requestMapping.method();
            final Map<HandlerKey, HandlerExecution> handlers = mapHandlerKey(requestMapping.value(), requestMethods)
                    .stream()
                    .collect(Collectors.toMap(it -> it, it -> new HandlerExecution(controllerInstance, method)));

            handlerExecutions.putAll(handlers);
        }
    }

    private Set<Method> getRequestMappingMethods(final Class<?> controller) {
        return ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private List<HandlerKey> mapHandlerKey(final String uri, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(it -> new HandlerKey(uri, it))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
