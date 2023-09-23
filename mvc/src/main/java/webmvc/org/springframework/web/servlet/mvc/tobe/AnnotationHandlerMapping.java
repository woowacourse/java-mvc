package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

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
        final Set<Class<?>> controllerClazzSet = reflections.getTypesAnnotatedWith(Controller.class);
        circuitClasses(controllerClazzSet);
    }

    private void circuitClasses(final Set<Class<?>> controllerClazzSet) {
        for (final Class<?> clazz : controllerClazzSet) {
            circuitMethods(clazz);
        }
    }

    private void circuitMethods(final Class<?> clazz) {
        for (final Method method : clazz.getDeclaredMethods()) {
            final RequestMapping requestMappingAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
            mapHandler(clazz, method, requestMappingAnnotation);
        }
    }

    private void mapHandler(final Class<?> clazz, final Method method, final RequestMapping requestMappingAnnotation) {
        if (requestMappingAnnotation != null) {
            final String requestUrl = requestMappingAnnotation.value();
            final RequestMethod requestMethod = requestMappingAnnotation.method()[0];
            final HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
            final Object controllerInstance = getControllerInstance(clazz);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object getControllerInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            throw new NoSuchElementException("Can not found such instance");
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
