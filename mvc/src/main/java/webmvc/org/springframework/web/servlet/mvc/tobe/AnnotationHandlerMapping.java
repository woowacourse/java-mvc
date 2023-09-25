package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            final Method[] methods = controller.getDeclaredMethods();
            final Object controllerInstance = getInstance(controller);

            createHandlerExecution(methods, controllerInstance);
        }
    }

    private void createHandlerExecution(final Method[] methods,
                                        final Object controllerInstance) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                final String path = requestMapping.value();
                final RequestMethod[] httpMethods = requestMapping.method();
                final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

                addHandlerExecutions(handlerExecution, path, httpMethods);
            }
        }
    }

    private void addHandlerExecutions(final HandlerExecution handlerExecution,
                                      final String path,
                                      final RequestMethod[] httpMethods) {
        for (RequestMethod httpMethod : httpMethods) {
            final HandlerKey handlerKey = new HandlerKey(path, httpMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object getInstance(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = HandlerKey.from(request);
        return handlerExecutions.get(handlerKey);
    }
}
