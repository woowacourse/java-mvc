package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exception.NoDefaultConstructorException;
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
        initHandlerExecutions(basePackage);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutions(final Object[] basePackage) {
        final Set<Class<?>> controllerClasses = findControllerClasses(basePackage);
        controllerClasses.forEach(
                controller -> addExecutions(newInstance(controller), findRequestMappingMethods(controller))
        );
    }

    private Set<Class<?>> findControllerClasses(final Object[] basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Object newInstance(final Class<?> clazz) {
        try {
            final Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (final Exception e) {
            throw new NoDefaultConstructorException();
        }
    }

    private List<Method> findRequestMappingMethods(final Class<?> clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addExecutions(final Object controllerInstance, final List<Method> requestMappingMethods) {
        for (final Method method : requestMappingMethods) {
            addExecution(controllerInstance, method);
        }
    }

    private void addExecution(final Object controllerInstance, final Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        for (final RequestMethod requestMethod : annotation.method()) {
            final HandlerKey key = new HandlerKey(annotation.value(), requestMethod);
            handlerExecutions.put(key, new HandlerExecution(controllerInstance, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        Objects.requireNonNull(request);

        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }
}
