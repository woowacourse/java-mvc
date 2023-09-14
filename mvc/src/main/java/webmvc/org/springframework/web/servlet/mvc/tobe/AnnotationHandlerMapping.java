package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controllerClass : controllerClasses) {
            try {
                final Object controller = controllerClass.getDeclaredConstructor().newInstance();
                final Set<Method> handlerMethods = getHandlerMethods(controllerClass);

                for (final Method method : handlerMethods) {
                    putHandler(controller, method);
                }
            } catch (InstantiationException |
                     IllegalAccessException |
                     InvocationTargetException |
                     NoSuchMethodException e
            ) {
                log.error("Fail Initializing Controller - " + e.getMessage(), e);
            }
        }
    }

    private Set<Method> getHandlerMethods(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void putHandler(final Object controller, final Method method) {
        final RequestMapping requestMappingInfo = method.getAnnotation(RequestMapping.class);

        for (RequestMethod requestMethod : requestMappingInfo.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMappingInfo.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(getHandlerKey(request));
    }

    private HandlerKey getHandlerKey(final HttpServletRequest request) {
        return new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
    }
}
