package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
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

    private void putControllerToHandlerExecutions(Class<?> controller) {
        Method[] controllerMethods = controller.getDeclaredMethods();
        Object instance = getInstance(controller);
        for (Method method : controllerMethods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            putHandlerKeyAndExecution(instance, method, requestMapping);
        }
    }

    private Object getInstance(Class<?> controller) {
        Object o;
        try {
            o = controller.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    private void putHandlerKeyAndExecution(Object instance, Method method, RequestMapping requestMapping) {
        RequestUrl requestUrl = new RequestUrl(requestMapping.value());
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(requestUrl, requestMethod, instance, method);
        }
    }
}
