package nextstep.mvc.controller.tobe;

import static java.util.stream.Collectors.toMap;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.scanner.ControllerScanner;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Set<Class<?>> mappedControllers = ControllerScanner.getInstance()
            .getAllAnnotations(basePackage);
        Map<Class<?>, Object> controllers = mappedControllers.stream()
            .collect(toMap(Function.identity(), this::getInitializedController));

        extractHandlerExecutions(controllers);
    }

    private Object getInitializedController(final Class<?> mappedController) {
        try {
            return mappedController.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void extractHandlerExecutions(final Map<Class<?>, Object> controllers) {
        for (Entry<Class<?>, Object> controller : controllers.entrySet()) {
            Set<Method> mappedMethods = getAllMethods(
                controller.getKey(), withAnnotation(RequestMapping.class));
            Map<Method, Set<HandlerKey>> methods = mappedMethods.stream()
                .collect(toMap(Function.identity(), this::getInitializedHandlerKeys));

            addHandlerExecution(controller.getValue(), methods);
        }
    }

    private Set<HandlerKey> getInitializedHandlerKeys(final Method mappedMethod) {
        RequestMapping requestMapping = mappedMethod.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();

        return Arrays.stream(requestMapping.method())
            .map(method -> new HandlerKey(url, method))
            .collect(Collectors.toSet());
    }

    private void addHandlerExecution(final Object controller,
                                     final Map<Method, Set<HandlerKey>> methods) {
        methods.forEach((key, value) -> {
            for (HandlerKey handlerKey : value) {
                handlerExecutions.put(handlerKey, new HandlerExecution(controller, key));
            }
        });
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        HandlerKey handlerKey = new HandlerKey(requestUri, RequestMethod.valueOf(method));
        log.debug("AnnotationHandler Request Mapping Uri : {}", requestUri);
        return handlerExecutions.get(handlerKey);
    }
}
