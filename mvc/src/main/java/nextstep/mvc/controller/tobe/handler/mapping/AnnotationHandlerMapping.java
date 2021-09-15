package nextstep.mvc.controller.tobe.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.exception.controller.NoSuchConstructorException;
import nextstep.mvc.handler.mapping.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackages) {
        this(basePackages, new HashMap<>());
    }

    private AnnotationHandlerMapping(
        Object[] basePackages,
        Map<HandlerKey, HandlerExecution> handlerExecutions
    ) {
        this.basePackages = basePackages;
        this.handlerExecutions = handlerExecutions;
    }

    public void initialize() {
        LOG.info("Initialize AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackages);

        Set<Class<?>> handlers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> handler : handlers) {
            Method[] methods = handler.getDeclaredMethods();
            getAnnotation(handler, methods);
        }

        for (HandlerKey key : handlerExecutions.keySet()) {
            LOG.info("Path: {}, Method: {}", key.getUrl(), key.getRequestMethod());
        }
    }

    private void getAnnotation(Class<?> handler, Method[] methods) {
        for (Method method : methods) {
            getAnnotationBasedOn(handler, method);
        }
    }

    private void getAnnotationBasedOn(Class<?> handler, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            String requestUrl = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();

            setHandlerExecutions(handler, method, requestUrl, requestMethods);
        }
    }

    private void setHandlerExecutions(
        Class<?> handler,
        Method method,
        String requestUrl,
        RequestMethod[] requestMethods
    ) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
            HandlerExecution handlerExecution = getHandlerExecution(handler, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private HandlerExecution getHandlerExecution(Class<?> handler, Method method) {
        try {
            Constructor<?> handlerConstructor = handler.getConstructor();
            return new HandlerExecution(handlerConstructor.newInstance(), method);
        } catch (Exception e) {
            throw new NoSuchConstructorException();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(
            request.getRequestURI(),
            RequestMethod.from(getMethod(request).toUpperCase())
        );

        return handlerExecutions.get(handlerKey);
    }

    private String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }
}
