package nextstep.mvc.controller.tobe.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.handler.mapping.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final HandlerScanner handlerScanner;

    public AnnotationHandlerMapping(Object... basePackages) {
        this(basePackages, new HashMap<>(), new HandlerScanner());
    }

    private AnnotationHandlerMapping(
        Object[] basePackages,
        Map<HandlerKey, HandlerExecution> handlerExecutions,
        HandlerScanner handlerScanner
    ) {
        this.basePackages = basePackages;
        this.handlerExecutions = handlerExecutions;
        this.handlerScanner = handlerScanner;
    }

    public void initialize() {
        LOG.info("Initialize AnnotationHandlerMapping!");

        handlerScanner.scan(basePackages);

        Map<Class<?>, Object> handlers = handlerScanner.getHandlers();
        for (Class<?> handler : handlers.keySet()) {
            Object handlerInstance = handlers.get(handler);
            Method[] methods = handler.getDeclaredMethods();

            initializeWith(handlerInstance, methods);
        }

        logPathAndMethod();
    }

    private void initializeWith(Object handlerInstance, Method[] methods) {
        for (Method method : methods) {
            initializeWithEach(handlerInstance, method);
        }
    }

    private void initializeWithEach(Object handlerInstance, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            String requestUrl = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();

            setHandlerExecutions(handlerInstance, method, requestUrl, requestMethods);
        }
    }

    private void setHandlerExecutions(
        Object handlerInstance,
        Method method,
        String requestUrl,
        RequestMethod[] requestMethods
    ) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handlerInstance, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private void logPathAndMethod() {
        for (HandlerKey key : handlerExecutions.keySet()) {
            LOG.info("Path: {}, Method: {}", key.getUrl(), key.getRequestMethod());
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
