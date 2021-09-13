package nextstep.mvc.controller.tobe;

import static java.util.stream.Collectors.toMap;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import nextstep.mvc.handler.mapping.HandlerMapping;
import nextstep.mvc.controller.scanner.ControllerScanner;
import nextstep.mvc.controller.scanner.RequestMappingScanner;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Set<Class<?>> allControllers = getAllControllers();

        var controllerAndMethods = allControllers.stream()
            .collect(toMap(Function.identity(), this::toKeyAndExecutionPair));

        initControllers(controllerAndMethods);
    }

    private void initControllers(Map<Class<?>, Set<Method>> controllerAndMethods) {
        for (var classAndMethods : controllerAndMethods.entrySet()) {
            var controller = classAndMethods.getKey();
            var methods = classAndMethods.getValue();

            var initializedController = getInitializedController(controller);
            var methodAndHandlerKeys = toMethodAndHandlerKeys(methods);

            putAnnotationBasedControllersToHandlerExecutions(
                initializedController,
                methodAndHandlerKeys
            );
        }
    }

    private Object getInitializedController(Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Map<Method, Set<HandlerKey>> toMethodAndHandlerKeys(Set<Method> methods) {
        return methods.stream()
            .collect(toMap(Function.identity(), this::requestMappingToHandlerKey));
    }

    private void putAnnotationBasedControllersToHandlerExecutions(
        Object initializedController,
        Map<Method, Set<HandlerKey>> methodAndHandlerKeys
    ) {
        for (Method method : methodAndHandlerKeys.keySet()) {
            handlerExecutions.putAll(extractHandlerKeyAndHandlerExecution(
                initializedController,
                methodAndHandlerKeys,
                method
            ));
        }
    }

    private Map<HandlerKey, HandlerExecution> extractHandlerKeyAndHandlerExecution(
        Object initializedController,
        Map<Method, Set<HandlerKey>> methodAndHandlerKeys, Method method
    ) {
        Set<HandlerKey> handlerKeys = methodAndHandlerKeys.get(method);

        return handlerKeys.stream()
            .collect(toMap(
                Function.identity(),
                e -> new HandlerExecution(initializedController, method)
            ));
    }


    public Set<Class<?>> getAllControllers() {
        return ControllerScanner.getAllControllers(basePackage);
    }

    private Set<Method> toKeyAndExecutionPair(Class<?> controller) {
        return RequestMappingScanner.getRequestMappedMethod(controller);
    }

    private Set<HandlerKey> requestMappingToHandlerKey(Method requestMappedMethod) {
        return RequestMappingScanner.extractHandlerKeys(requestMappedMethod);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        var handlerKey =
            new HandlerKey(requestURI, RequestMethod.valueOf(method.toUpperCase(Locale.ROOT)));

        return handlerExecutions.get(handlerKey);
    }
}
