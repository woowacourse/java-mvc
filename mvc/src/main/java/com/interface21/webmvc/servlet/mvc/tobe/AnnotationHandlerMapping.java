package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.scan();

        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> controllerClass = entry.getKey();
            Object controller = entry.getValue();

            for (Method method : controllerClass.getMethods()) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                if (mapping == null) {
                    continue;
                }

                RequestMethod[] methods = mapping.method();
                if (methods.length == 0) {
                    methods = RequestMethod.values();
                }

                for (RequestMethod requestMethod : methods) {
                    HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
                    HandlerExecution execution = new HandlerExecution(controller, method);
                    HandlerExecution existingExecution = handlerExecutions.putIfAbsent(key, execution);
                    if (existingExecution != null) {
                        throw new IllegalStateException("Duplicate handler mapping for " + key + " found in "
                                + controllerClass.getName() + "#" + method.getName());
                    }
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        String methodName = request.getMethod();
        RequestMethod method = resolveMethod(methodName);

        if (method != null) {
            HandlerExecution execution = handlerExecutions.get(new HandlerKey(url, method));
            if (execution != null) {
                return execution;
            }
        }

        Set<RequestMethod> supportedMethods = findSupportedMethods(url);
        if (supportedMethods.isEmpty()) {
            return null;
        }

        throw new RequestMethodNotSupportedException(methodName, supportedMethods);
    }

    private RequestMethod resolveMethod(final String methodName) {
        try {
            return RequestMethod.valueOf(methodName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Set<RequestMethod> findSupportedMethods(final String url) {
        Set<RequestMethod> supportedMethods = EnumSet.noneOf(RequestMethod.class);
        for (HandlerKey key : handlerExecutions.keySet()) {
            if (key.getUrl().equals(url)) {
                supportedMethods.add(key.getRequestMethod());
            }
        }
        return supportedMethods;
    }
}
