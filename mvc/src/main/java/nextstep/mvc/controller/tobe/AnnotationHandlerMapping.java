package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
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
        for (final var basePackage : this.basePackage) {
            final var reflections = new Reflections(basePackage);
            for (final var clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
                addController(clazz);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addController(Class<?> clazz) {
        try {
            final var controller = clazz.getDeclaredConstructor().newInstance();
            Arrays.stream(clazz.getMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandler(controller, method));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    private void addHandler(Object controller, Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        for (final var httpMethod : requestMapping.method()) {
            final var key = new HandlerKey(requestMapping.value(), httpMethod);
            handlerExecutions.put(key, new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var url = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var key = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(key);
    }
}
