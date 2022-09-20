package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exception.RequestMappingNotAnnotatedException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import nextstep.web.support.RequestUrl;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions(new HashMap<>());
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : annotatedControllers) {
            putControllerToHandlerExecutions(controller);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestUrl requestURI = new RequestUrl(request.getRequestURI());
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        return handlerExecutions.get(requestURI, requestMethod);
    }

    private void putControllerToHandlerExecutions(final Class<?> controller) {
        Method[] controllerMethods = controller.getDeclaredMethods();
        Object instance = getInstance(controller);
        for (Method method : controllerMethods) {
            RequestMapping requestMapping = getRequestMappingAnnotation(method);
            putHandlerKeyAndExecution(instance, method, requestMapping);
        }
    }

    private Object getInstance(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void putHandlerKeyAndExecution(final Object instance, final Method method, final RequestMapping requestMapping) {
        RequestUrl requestUrl = new RequestUrl(requestMapping.value());
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(requestUrl, requestMethod, instance, method);
        }
    }

    private RequestMapping getRequestMappingAnnotation(final Method method) {
        try {
            return Objects.requireNonNull(method.getDeclaredAnnotation(RequestMapping.class));
        } catch (NullPointerException e) {
            throw new RequestMappingNotAnnotatedException();
        }
    }
}
