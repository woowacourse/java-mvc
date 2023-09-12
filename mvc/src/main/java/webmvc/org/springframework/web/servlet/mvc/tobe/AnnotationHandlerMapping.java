package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
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

    public void initialize() throws Exception {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> type : types) {
            addHandlerExecutionsByType(type);
        }
    }

    private void addHandlerExecutionsByType(final Class<?> type) throws Exception {
        final Constructor<?>[] constructors = type.getConstructors();
        final Object controller = constructors[0].newInstance();
        for (final Method method : type.getDeclaredMethods()) {
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            addHandlerExecution(annotation, handlerExecution);
        }
    }

    private void addHandlerExecution(final RequestMapping annotation, final HandlerExecution handlerExecution) {
        for (final RequestMethod requestMethod : annotation.method()) {
            final HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
