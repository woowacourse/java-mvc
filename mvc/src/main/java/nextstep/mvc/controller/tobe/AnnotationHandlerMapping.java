package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object targetPackage : basePackage) {
            final Reflections reflections = new Reflections(targetPackage);
            final Set<Class<?>> classes = new HashSet<>(reflections.getTypesAnnotatedWith(Controller.class));
            addHandlerExecution(classes);
        }
    }

    private void addHandlerExecution(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            final List<Method> methods = getMethods(clazz);
            generateHandlerExecutions(methods, clazz);
        }
    }

    private List<Method> getMethods(Class<?> clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.getDeclaredAnnotation(RequestMapping.class) != null)
                .collect(Collectors.toList());
    }

    private void generateHandlerExecutions(final List<Method> methods, final Class<?> clazz) {
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            final Object handler = getHandlerInstance(clazz);
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
            final HandlerExecution handlerExecution = new HandlerExecution(method, handler);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object getHandlerInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        log.debug("AnnotationHandlerMapping Method: {}, RequestURI: {}", request.getMethod(), request.getRequestURI());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
