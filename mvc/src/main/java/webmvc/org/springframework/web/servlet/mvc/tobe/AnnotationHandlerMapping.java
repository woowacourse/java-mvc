package webmvc.org.springframework.web.servlet.mvc.tobe;

import com.sun.jdi.InternalException;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final int EMPTY_REQUEST_METHOD = 0;

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        registerHandlerWithAnnotation(basePackages);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerWithAnnotation(final Object... basePackages) {
        final Set<Class<?>> classes = getClassesByAnnotation(basePackages, Controller.class);
        for (final Class<?> clazz : classes) {
            final List<Method> methods = getMethodsByAnnotation(clazz, RequestMapping.class);
            for (final Method method : methods) {
                final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                validateAnnotation(annotation);
                addToHandlerExecutions(clazz, method, annotation);
            }
        }
    }

    private <T extends Annotation> Set<Class<?>> getClassesByAnnotation(final Object[] basePackages,
                                                                        final Class<T> annotation) {
        final Reflections reflections = new Reflections(basePackages);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    private <T extends Annotation> List<Method> getMethodsByAnnotation(final Class<?> clazz,
                                                                       final Class<T> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    private void validateAnnotation(final RequestMapping annotation) {
        final RequestMethod[] method = annotation.method();
        if (method.length == EMPTY_REQUEST_METHOD) {
            throw new HandlerMappingException("RequestMethod 값이 없습니다.");
        }
    }

    private void addToHandlerExecutions(final Class<?> clazz, final Method method, final RequestMapping annotation) {
        for (final RequestMethod requestMethod : annotation.method()) {
            final Object handler = makeHandlerInstance(clazz);
            final HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Path : {}, Method: {}, Controller : {}", annotation.value(), requestMethod, clazz.getTypeName());
        }
    }

    private Object makeHandlerInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException |
                       InvocationTargetException e) {
            throw new InternalException("handler 인스턴스 생성에 실패했습니다.");
        }
    }

    @Override
    public Optional<Object> getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        final HandlerExecution handler = handlerExecutions.get(handlerKey);
        if (handler == null) {
            return Optional.empty();
        }
        return Optional.of(handler);
    }
}
