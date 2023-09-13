package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

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

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.putAll(extractHandler());
    }

    private Map<HandlerKey, HandlerExecution> extractHandler() {
        Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).getControllers();
        return controllers.keySet().stream()
            .map(targetClass -> extractHandlerFromClass(targetClass, controllers.get(targetClass)))
            .reduce(new HashMap<>(), migrateHandler());
    }

    private Map<HandlerKey, HandlerExecution> extractHandlerFromClass(Class<?> targetClass, Object handler) {
        return Arrays.stream(targetClass.getMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .map(method -> extractHandlerFromMethod(method, handler))
            .reduce(new HashMap<>(), migrateHandler());
    }

    private Map<HandlerKey, HandlerExecution> extractHandlerFromMethod(Method method, Object handler) {
        HandlerExecution handlerExecution = new HandlerExecution(handler, method);
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        return Arrays.stream(annotation.method())
            .map(requestMethod -> {
                Map<HandlerKey, HandlerExecution> extractedHandlerMapping = new HashMap<>();
                extractedHandlerMapping.put(new HandlerKey(annotation.value(), requestMethod), handlerExecution);
                return extractedHandlerMapping;
            }).reduce(new HashMap<>(), migrateHandler());
    }

    private BinaryOperator<Map<HandlerKey, HandlerExecution>> migrateHandler() {
        return (originHandler, migrateHandler) -> {
            checkDuplication(originHandler, migrateHandler);
            originHandler.putAll(migrateHandler);
            return originHandler;
        };
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

    @Override
    public Optional<Object> getHandler(final HttpServletRequest request) {
        Optional<HandlerKey> findHandler = handlerExecutions.keySet().stream()
            .filter(handlerKey -> handlerKey.canHandle(request))
            .findAny();
        if (findHandler.isPresent()) {
            return Optional.of(handlerExecutions.get(findHandler.get()));
        }
        return Optional.empty();
    }
}
