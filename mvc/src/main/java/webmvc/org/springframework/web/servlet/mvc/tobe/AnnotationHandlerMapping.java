package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final List<Object> basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = List.of(basePackages);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        makeHandlerExecutions(basePackages);
    }

    private void makeHandlerExecutions(final List<Object> basePackages) {
        for (final Object basePackage : basePackages) {
            final Reflections reflections = new Reflections(basePackage);
            final var classes = reflections.getTypesAnnotatedWith(Controller.class);
            putHandlerExecutionsByControllers(classes);
        }
    }

    private void putHandlerExecutionsByControllers(final Set<Class<?>> classes) {
        for (final Class<?> clazz : classes) {
            final Constructor<?> constructor = getConstructor(clazz);
            final List<Method> methods = getMethodsWithAnnotation(clazz);
            putHandlerExecutionsByMethods(constructor, methods);
        }
    }

    private Constructor<?> getConstructor(final Class<?> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("기본 생성자가 존재하지 않습니다.");
        }
    }

    private List<Method> getMethodsWithAnnotation(final Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private void putHandlerExecutionsByMethods(final Constructor<?> constructor, final List<Method> methods) {
        for (final Method method : methods) {
            putHandlerExecutionsByMethod(constructor, method);
        }
    }

    private void putHandlerExecutionsByMethod(final Constructor<?> constructor, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final Map<HandlerKey, HandlerExecution> handlerExecutionsByMethod =
            convertHandlerExecutions(constructor, method, requestMapping);

        handlerExecutions.putAll(handlerExecutionsByMethod);
    }

    private Map<HandlerKey, HandlerExecution> convertHandlerExecutions(
        final Constructor<?> constructor,
        final Method method,
        final RequestMapping requestMapping
    ) {
        final String requestURI = requestMapping.value();
        final List<RequestMethod> requestMethods = List.of(requestMapping.method());

        return requestMethods.stream()
            .map(requestMethod -> new HandlerKey(requestURI, requestMethod))
            .collect(Collectors.toMap(
                handlerKey -> handlerKey,
                handlerKey -> new HandlerExecution(constructor, method)
            ));
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = makeHandlerKey(request);
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey makeHandlerKey(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        return new HandlerKey(requestURI, requestMethod);
    }
}
