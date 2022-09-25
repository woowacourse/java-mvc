package nextstep.mvc.controller.tobe;

import static org.reflections.ReflectionUtils.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final HandlerKey ROOT_HANDLER_KEY = new HandlerKey("/", RequestMethod.GET);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final Object[] basePackage;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).getControllers();
        final Set<Class<?>> classes = controllers.keySet();
        for (final Method method : getRequestMappingMethods(classes)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers, final Method method,
        final RequestMapping requestMapping) {

        final String requestUri = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        final Object controller = controllers.get(method.getDeclaringClass());

        for (final HandlerKey handlerKey : mapHandlerKeys(requestUri, requestMethods)) {
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
            log.info("Controller method registered. class: {}, uri: {}, method: {}",
                controller.getClass().getName(), requestUri, method.getName());
        }
    }

    private List<HandlerKey> mapHandlerKeys(final String requestUri, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
            .map(requestMethod -> new HandlerKey(requestUri, requestMethod))
            .collect(Collectors.toList());
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> classes) {
        Set<Method> methods = new HashSet<>();
        for (final Class<?> clazz : classes) {
            methods.addAll(getAllMethods(clazz, withAnnotation(RequestMapping.class)));
        }
        return methods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
            RequestMethod.valueOf(request.getMethod()));
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        return handlerExecutions.get(ROOT_HANDLER_KEY);
    }
}
