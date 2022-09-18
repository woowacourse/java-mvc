package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(this::addAllHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }

    private void addAllHandlerExecutions(final Class<?> controller) {
        final List<Method> declaredMethods = Arrays.stream(controller.getDeclaredMethods())
                .filter(it -> isAnnotatedWith(it, RequestMapping.class))
                .collect(Collectors.toList());
        final Object handler = createInstance(controller);

        for (final Method declaredMethod : declaredMethods) {
            final RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            addHandlerExecution(annotation, declaredMethod, handler);
        }
    }

    private boolean isAnnotatedWith(final Method method, final Class<?> annotationClass) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }

    private void addHandlerExecution(final RequestMapping annotation,
                                     final Method declaredMethod,
                                     final Object handler) {
        for (final RequestMethod method : annotation.method()) {
            final HandlerKey handlerKey = new HandlerKey(annotation.value(), method);
            final HandlerExecution handlerExecution = new HandlerExecution(handler, declaredMethod);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object createInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            log.error("add execution exception : {}", e.getMessage(), e);
            throw new RuntimeException();
        }
    }
}
