package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.reflections.util.ReflectionUtilsPredicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

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
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        final Set<Method> methods = getRequestMappingMethods(controllers.keySet());

        for (final Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Method> getRequestMappingMethods(
            final Set<Class<?>> classes
    ) {
        final Set<Method> methods = new HashSet<>();
        for (final Class<?> clazz : classes) {
            methods.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtilsPredicates.withAnnotation(RequestMapping.class)));
        }

        return methods;
    }

    private void addHandlerExecutions(
            final Map<Class<?>, Object> controllers,
            final Method method,
            final RequestMapping requestMapping
    ) {
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        final List<HandlerKey> handlerKeys = mapHandlerKeys(url, requestMethods);

        for (final HandlerKey handlerKey : handlerKeys) {
            final Object classInstance = controllers.get(method.getDeclaringClass());
            final HandlerExecution handlerExecution = new HandlerExecution(classInstance, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<HandlerKey> mapHandlerKeys(final String url, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                     .map(requestMethod -> new HandlerKey(url, requestMethod))
                     .collect(toList());
    }

    @Override
    public boolean containsHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = HandlerKey.from(request);
        return handlerExecutions.containsKey(handlerKey);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = HandlerKey.from(request);
        return handlerExecutions.get(handlerKey);
    }
}
