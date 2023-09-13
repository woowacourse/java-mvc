package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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
        try {
            final Reflections reflections = new Reflections(basePackage);
            final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            for (final Class<?> controller : controllers) {
                final Object controllerInstance = controller.getDeclaredConstructor().newInstance();
                final Method[] declaredMethods = controller.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                        final RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
                        final String url = requestMapping.value();
                        final RequestMethod[] requestMethods = requestMapping.method();
                        for (RequestMethod requestMethod : requestMethods) {
                            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, declaredMethod);
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    }
                }
            }
        } catch (NoSuchMethodException
                 | InvocationTargetException
                 | InstantiationException
                 | IllegalAccessException e) {
            log.error("", e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.find(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
