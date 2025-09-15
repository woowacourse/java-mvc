package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> controllerType : controllerTypes) {
                Constructor<?> constructor = controllerType.getConstructor();
                Object controller = constructor.newInstance();

                Method[] declaredMethods = controllerType.getDeclaredMethods();
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
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
