package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllers) {
            try {
                Object controller = controllerClass.getDeclaredConstructor().newInstance();
                Method[] methods = controllerClass.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                        log.info("URL: {}, HTTP methods: {}",
                                mapping.value(),
                                Arrays.toString(mapping.method()));

                        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                        RequestMethod[] requestMethods = mapping.method();
                        if (requestMethods.length == 0) {
                            requestMethods = RequestMethod.values();
                        }
                        for (RequestMethod requestMethod : requestMethods) {
                            HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
                            handlerExecutions.put(key, handlerExecution);
                        }
                    }
                }

            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException("Controller 생성 실패: " + controllerClass.getName(), e);
            }
        }

    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(key);
    }
}
