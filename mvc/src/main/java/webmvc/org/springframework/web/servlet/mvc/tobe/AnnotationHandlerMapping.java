package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

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
        for (Object subPackage : basePackage) {
            initTargetPackage(subPackage);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initTargetPackage(final Object base) {
        final Reflections reflections = new Reflections(base);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::initTargetController);
    }

    private void initTargetController(final Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            initTargetMethod(method);
        }
    }

    private void initTargetMethod(final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final List<HandlerKey> keys = getHandlerKeys(method);
            final HandlerExecution handlerExecution = new HandlerExecution(method);

            final Map<HandlerKey, HandlerExecution> handlerExecutionMap = keys.stream()
                    .map(key -> Map.entry(key, handlerExecution))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            handlerExecutions.putAll(handlerExecutionMap);
        }
    }

    private List<HandlerKey> getHandlerKeys(final Method method) {
        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        final String uri = requestMapping.value();
        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(uri, requestMethod))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final HandlerKey key = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(key);
    }
}
