package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
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
    private static final Class<RequestMapping> REQUEST_MAPPING_ANNOTATION = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : classes) {
            Object instance = getInstance(clazz);
            final List<Method> methods = getRequestMappingMethods(clazz);
            putHandlerExecutionByMethods(instance, methods);
        }
    }

    private Object getInstance(final Class<?> clazz) {
        try {
            final Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private List<Method> getRequestMappingMethods(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(REQUEST_MAPPING_ANNOTATION))
                .collect(Collectors.toList());
    }

    private void putHandlerExecutionByMethods(final Object instance, final List<Method> methods) {
        for (Method method : methods) {
            putHandlerExecutionByAnnotation(instance, method);
        }
    }

    private void putHandlerExecutionByAnnotation(final Object instance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(REQUEST_MAPPING_ANNOTATION);
        putHandlerExecutionByHttpMethods(instance, method, requestMapping);
    }

    private void putHandlerExecutionByHttpMethods(final Object instance, final Method method, final RequestMapping requestMapping) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);

        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
