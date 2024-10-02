package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> value = new HashMap<>();

    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach((k, v) -> initHandlerExecutions(k, Arrays.stream(k.getDeclaredMethods()).toList()));

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutions(Class<?> controllerClazz, List<Method> methods) {
        for (Method method : methods) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            String url = mapping.value();
            RequestMethod[] requestMethods = getRequestMethods(mapping, mapping.method());
            putHandlerKeyAndExecution(controllerClazz, method, requestMethods, url);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping mapping, RequestMethod[] methods) {
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return mapping.method();
    }

    private void putHandlerKeyAndExecution(
            Class<?> controllerClazz,
            Method method,
            RequestMethod[] requestMethods,
            String url
    ) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = createHandlerExecution(controllerClazz, method);
            value.put(handlerKey, handlerExecution);
        }
    }

    private HandlerExecution createHandlerExecution(Class<?> controllerClazz, Method method) {
        return new HandlerExecution(controllerScanner.getControllers().get(controllerClazz), method);
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return value.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}
