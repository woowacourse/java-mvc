package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Map<String, Controller> controllers = new HashMap<>();

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            Class<?> clazz = Class.forName("samples.TestController");
            Object controller = clazz.getConstructor().newInstance();

            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);

                    String url = mapping.value();
                    RequestMethod[] methods = mapping.method();

                    for (RequestMethod requestMethod : methods) {
                        HandlerKey handlerKey = new HandlerKey(url, requestMethod);

                        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        
        } catch (Exception e) {
            throw new RuntimeException();
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestUri, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
