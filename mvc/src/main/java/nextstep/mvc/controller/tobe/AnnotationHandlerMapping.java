package nextstep.mvc.controller.tobe;

import static java.util.stream.Collectors.toMap;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.scanner.ControllerScanner;
import nextstep.mvc.controller.scanner.RequestMappingScanner;
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

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> controllers = getAllControllers(basePackage);
        Map<Class<?>, Set<Method>> controllersAndMethods = controllers.stream()
            .collect(toMap(Function.identity(), this::convertObjectToMethods));

        initControllers(controllersAndMethods);
    }

    private Set<Class<?>> getAllControllers(final String... basePackage) {
        return ControllerScanner.getInstance().getAllAnnotations(basePackage);
    }

    private Set<Method> convertObjectToMethods(final Class<?> controller) {
        return RequestMappingScanner.getInstance().getAllAnnotations(controller);
    }

    private void initControllers(final Map<Class<?>, Set<Method>> controllersAndMethods) {
        for (Entry<Class<?>, Set<Method>> classAndMethods : controllersAndMethods.entrySet()) {
            Class<?> controller = classAndMethods.getKey();
            Set<Method> methods = classAndMethods.getValue();

            Object initializedController = getInitializedController(controller);
            Map<Method, Set<HandlerKey>> initializedMethods = convertObjectToMethodAndHandlerKeys(
                methods);

            addHandlerExecutions(initializedController, initializedMethods);
        }
    }

    private Object getInitializedController(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Map<Method, Set<HandlerKey>> convertObjectToMethodAndHandlerKeys(
        final Set<Method> methods) {
        return methods.stream()
            .collect(toMap(Function.identity(), this::convertObjectToHandlerKeys));
    }

    private Set<HandlerKey> convertObjectToHandlerKeys(final Method methods) {
        return RequestMappingScanner.getInstance().extractHandlerKeys(methods);
    }

    private void addHandlerExecutions(final Object initializedController,
                                      final Map<Method, Set<HandlerKey>> initializedMethods) {
        for (Method method : initializedMethods.keySet()) {
            handlerExecutions.putAll(
                extractHandlerKeyAndHandlerExecutions(
                    initializedController,
                    initializedMethods,
                    method
                )
            );
        }
    }

    private Map<? extends HandlerKey, ? extends HandlerExecution> extractHandlerKeyAndHandlerExecutions(
        final Object initializedController,
        final Map<Method, Set<HandlerKey>> initializedMethods,
        final Method method) {

        Set<HandlerKey> handlerKeys = initializedMethods.get(method);

        return handlerKeys.stream()
            .collect(toMap(
                    Function.identity(),
                    e -> new HandlerExecution(initializedController, method)));
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        HandlerKey handlerKey = new HandlerKey(requestUri, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
