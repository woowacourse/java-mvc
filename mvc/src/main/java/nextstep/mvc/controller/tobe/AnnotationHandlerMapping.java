package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (final var clazz : scanControllerClasses()) {
            mapController(clazz);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Class<?>> scanControllerClasses() {
        return Arrays.stream(this.basePackage)
                .map(Reflections::new)
                .map(it -> it.getTypesAnnotatedWith(Controller.class))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void mapController(Class<?> clazz) {
        final var controller = initializeController(clazz);
        for (final var method : clazz.getMethods()) {
            mapHandler(controller, method);
        }
    }

    private Object initializeController(Class<?> clazz) {
        try {
            final var constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
            throw new IllegalArgumentException("존재할 수 없는 경우");
        }
    }

    private void mapHandler(Object controller, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        for (final var httpMethod : requestMapping.method()) {
            final var key = new HandlerKey(requestMapping.value(), httpMethod);
            handlerExecutions.put(key, new HandlerExecution(controller, method));
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final var url = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var key = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(key);
    }
}
