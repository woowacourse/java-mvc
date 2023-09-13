package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        validateBasePackage(basePackage);
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    private void validateBasePackage(Object[] basePackage) {
        for (Object o : basePackage) {
            if (!(o instanceof String)) {
                throw new IllegalArgumentException("basePackage 는 String 으로 입력해야 합니다");
            }
        }
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.putAll(findHandlersFrom(basePackage));
    }

    private Map<HandlerKey, HandlerExecution> findHandlersFrom(Object[] basePackage) {
        List<String> basePackages = convertToString(basePackage);
        Map<HandlerKey, HandlerExecution> handlers = new HashMap<>();
        for (String targetPackage : basePackages) {
            Map<HandlerKey, HandlerExecution> packageHandler = extractHandler(targetPackage);
            checkDuplication(handlers, packageHandler);
            handlers.putAll(packageHandler);
        }
        return handlers;
    }

    private static List<String> convertToString(Object[] basePackage) {
        return Arrays.stream(basePackage)
            .map(o -> (String) o)
            .collect(Collectors.toList());
    }

    private Map<HandlerKey, HandlerExecution> extractHandler(String targetPackage) {
        Reflections reflections = new Reflections(targetPackage);
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
            .map(this::extractHandlerFromClass)
            .reduce(
                new HashMap<>(),
                migrateHandler());
    }

    private void checkDuplication(Map<HandlerKey, HandlerExecution> originHandlers,
                                  Map<HandlerKey, HandlerExecution> newHandlers) {
        Set<HandlerKey> duplicatedHandlerKeys = new HashSet<>(originHandlers.keySet());
        duplicatedHandlerKeys.retainAll(newHandlers.keySet());
        if (!duplicatedHandlerKeys.isEmpty()) {
            HandlerKey duplicatedHandlerKey = duplicatedHandlerKeys.iterator().next();
            log.error("duplication handler : {}", duplicatedHandlerKey);
            throw new IllegalArgumentException("Duplicated HandlerKey");
        }
    }

    private Map<HandlerKey, HandlerExecution> extractHandlerFromClass(Class<?> targetClass) {
        Object handler = makeClass(targetClass);
        return Arrays.stream(targetClass.getMethods())
            .filter(this::haveRequestMapping)
            .map(method -> extractHandlerFromMethod(method, handler))
            .reduce(
                new HashMap<>(),
                migrateHandler()
            );
    }

    private Object makeClass(Class<?> targetClass) {
        try {
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Map<HandlerKey, HandlerExecution> extractHandlerFromMethod(Method method,
                                                                       Object handler) {
        return Arrays.stream(method.getAnnotation(RequestMapping.class).method())
            .map(makeHandler(method, handler, method.getAnnotation(RequestMapping.class)))
            .reduce(
                new HashMap<>(),
                migrateHandler()
            );
    }

    private Function<RequestMethod, Map<HandlerKey, HandlerExecution>> makeHandler(Method method,
                                                                                   Object handler,
                                                                                   RequestMapping requestMapping) {
        return requestMethod -> {
            Map<HandlerKey, HandlerExecution> extractedHandlerMapping = new HashMap<>();
            extractedHandlerMapping.put(
                new HandlerKey(requestMapping.value(), requestMethod),
                new HandlerExecution(handler, method)
            );
            return extractedHandlerMapping;
        };
    }

    private BinaryOperator<Map<HandlerKey, HandlerExecution>> migrateHandler() {
        return (extractedHandler, extractingController) -> {
            checkDuplication(extractedHandler, extractingController);
            extractedHandler.putAll(extractingController);
            return extractedHandler;
        };
    }

    private boolean haveRequestMapping(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
            .anyMatch(RequestMapping.class::isInstance);
    }

    public Object getHandler(final HttpServletRequest request) {
        Optional<HandlerKey> findHandler = handlerExecutions.keySet().stream()
            .filter(handlerKey -> handlerKey.canHandle(request))
            .findAny();
        return findHandler.map(handlerExecutions::get)
            .orElse(null);
    }
}
