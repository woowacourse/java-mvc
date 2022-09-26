package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
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

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final Map<Class<?>, Object> controllers = controllerScanner.scan(basePackage);

        for (Class<?> controller : controllers.keySet()) {
            final Object instance = controllers.get(controller);
            putHandlersByEachController(controller, instance);
        }
    }

    private void putHandlersByEachController(final Class<?> controller, final Object instance) {
        for (Method handlerMethod : handlerMethodsByEach(controller)) {
            putHandlersByEachMethod(handlerMethod, instance);
        }
    }

    @SuppressWarnings("unchecked")
    private Set<Method> handlerMethodsByEach(final Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void putHandlersByEachMethod(final Method method, final Object instance) {
        final HandlerExecution execution = new HandlerExecution(method, instance);
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        for (HandlerKey key : handlerKeysByEach(requestMapping)) {
            handlerExecutions.put(key, execution);
        }
    }

    private List<HandlerKey> handlerKeysByEach(RequestMapping requestMapping) {
        final String url = requestMapping.value();

        return Stream.of(requestMapping.method())
                .map(method -> new HandlerKey(url, method))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String requestMethodName = request.getMethod().toUpperCase();
        final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(requestMethodName));
        return handlerExecutions.get(handlerKey);
    }
}
