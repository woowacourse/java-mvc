package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

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
        for (final Object targetPackage : basePackage) {
            addAllHandlerExecutionsInBasePackage(targetPackage);
        }
    }

    private void addAllHandlerExecutionsInBasePackage(final Object base) {
        final Reflections reflections = new Reflections(base);
        final Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> clazz : controllerClass) {
            addHandlerExecutionsInController(clazz);
        }
    }

    private void addHandlerExecutionsInController(final Class<?> clazz) {
        final Method[] methods = clazz.getMethods();

        for (final Method method : methods) {
            addMethodAbleToHandleRequest(method);
        }
    }

    private void addMethodAbleToHandleRequest(final Method method) {
        final RequestMapping handlerAnnotation = method.getAnnotation(RequestMapping.class);
        if (handlerAnnotation == null) {
            return;
        }
        putAllMatchingHandlerExecutions(method, handlerAnnotation);
    }

    private void putAllMatchingHandlerExecutions(final Method method, final RequestMapping requestMappingAnnotation) {
        final RequestMethod[] requestMethods = requestMappingAnnotation.method();
        final String path = requestMappingAnnotation.value();
        final HandlerExecution handlerExecution = new HandlerExecution(method);

        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = retreiveHandlerKey(request);

        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey retreiveHandlerKey(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return new HandlerKey(requestURI, requestMethod);
    }
}
