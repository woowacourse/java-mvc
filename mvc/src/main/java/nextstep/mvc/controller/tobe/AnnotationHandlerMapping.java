package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
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

    public void initialize() {
        final var reflections = new Reflections(basePackage);
        final var controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::init);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void init(final Class<?> clazz) {
        final Object handler;
        try {
            handler = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        final var annotatedMethods = ReflectionUtils.getAllMethods(clazz,
                ReflectionUtils.withAnnotation(RequestMapping.class));
        for (final Method method : annotatedMethods) {
            final var requestMapping = method.getAnnotation(RequestMapping.class);
            handlerExecutions.put(
                    new HandlerKey(requestMapping.value(), requestMapping.method()[0]),
                    new HandlerExecution(handler, method)
            );
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var url = request.getRequestURI();
        final var method = request.getMethod();
        final var handlerKey = new HandlerKey(url, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
