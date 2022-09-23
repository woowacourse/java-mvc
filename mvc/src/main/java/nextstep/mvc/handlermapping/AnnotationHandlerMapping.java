package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.exception.HandlerNotFoundException;
import nextstep.mvc.support.ControllerScanner;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        ControllerScanner.getControllers(basePackage)
                .forEach((clazz, controller) -> {
                    final Set<Method> requestMappingMethods = getRequestMappingMethods(clazz);
                    requestMappingMethods.forEach(it -> addHandlerExecutions(it, controller));
                });
    }

    private Set<Method> getRequestMappingMethods(final Class<?> controller) {
        final Method[] methods = controller.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(this::filterRequestMapping)
                .collect(Collectors.toSet());
    }

    private boolean filterRequestMapping(final Method method) {
        return method.getDeclaredAnnotation(RequestMapping.class) != null;
    }

    private void addHandlerExecutions(final Method method, final Object controller) {
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        mapHandlerKey(requestMapping.value(), requestMapping.method())
                .forEach(it -> handlerExecutions.put(it, handlerExecution));
    }

    private List<HandlerKey> mapHandlerKey(final String url, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(it -> new HandlerKey(url, it))
                .collect(Collectors.toList());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new HandlerNotFoundException();
        }
        return handlerExecutions.get(handlerKey);
    }
}
