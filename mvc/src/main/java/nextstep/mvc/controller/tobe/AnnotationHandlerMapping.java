package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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

        final ControllerScanner controllerScanner = ControllerScanner.from(basePackages);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        initializeByControllers(controllers);
    }

    private void initializeByControllers(final Map<Class<?>, Object> controllers) {
        for (Class<?> controllerClass : controllers.keySet()) {
            final List<Method> methods = getRequestMappingMethods(controllerClass);
            final Object controller = controllers.get(controllerClass);
            addHandlersByControllerMethods(methods, controller);
        }
    }

    private void addHandlersByControllerMethods(final List<Method> methods, final Object controller) {
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            final List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping.value(), requestMapping.method());
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            addHandlerExecutions(handlerKeys, handlerExecution);
        }
    }

    private List<Method> getRequestMappingMethods(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private List<HandlerKey> getHandlerKeys(final String requestValue, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(requestValue, requestMethod))
                .collect(Collectors.toList());
    }

    private void addHandlerExecutions(final List<HandlerKey> handlerKeys, final HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));

        return handlerExecutions.get(handlerKey);
    }
}
