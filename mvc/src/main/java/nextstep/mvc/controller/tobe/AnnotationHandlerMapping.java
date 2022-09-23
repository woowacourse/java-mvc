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
import org.w3c.dom.html.HTMLAreaElement;

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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> handlerClasses = new HashSet<>(reflections.getTypesAnnotatedWith(Controller.class));
        addHandlerExecution(handlerClasses);
    }

    private void addHandlerExecution(final Set<Class<?>> handlerClasses) {
        for (Class<?> handlerClass : handlerClasses) {
            final List<Method> requestMappedMethods = getMethods(handlerClass);
            generateHandlerExecutions(requestMappedMethods, handlerClass);
        }
    }

    private List<Method> getMethods(final Class<?> handlerClass) {
        final Method[] requestMappedMethods = handlerClass.getDeclaredMethods();
        return Arrays.stream(requestMappedMethods)
                .filter(method -> method.getDeclaredAnnotation(RequestMapping.class) != null)
                .collect(Collectors.toList());
    }

    private void generateHandlerExecutions(final List<Method> requestMappedMethods, final Class<?> handlerClass) {
        for (Method method : requestMappedMethods) {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            final Object handler = getHandlerInstance(handlerClass);
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
            final HandlerExecution handlerExecution = new HandlerExecution(method, handler);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object getHandlerInstance(Class<?> handlerClass) {
        try {
            Constructor<?> constructor = handlerClass.getConstructor();
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
