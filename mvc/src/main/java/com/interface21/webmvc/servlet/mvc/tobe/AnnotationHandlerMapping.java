package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        if (basePackages == null || basePackages.length == 0) {
            throw new IllegalArgumentException("backPackage가 설정되어야 합니다");
        }

        try {
            final Reflections reflections = new Reflections(this.basePackages);
            final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> controllerClass : controllerClasses) {
                final Object handler = controllerClass.getConstructor().newInstance();
                final Method[] methods = controllerClass.getMethods();

                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String url = requestMapping.value();
                        RequestMethod[] requestMethods = requestMapping.method();

                        RequestMethod[] targetMethods = requestMethods;

                        if (requestMethods.length == 0) {
                            targetMethods = RequestMethod.values();
                        }

                        for (RequestMethod targetMethod : targetMethods) {
                            HandlerKey handlerKey = new HandlerKey(url, targetMethod);
                            HandlerExecution handlerExecution = new HandlerExecution(handler, method);

                            if (handlerExecutions.containsKey(handlerKey)) {
                                throw new IllegalStateException("같은 RequestMapping url에 대한 중복 매핑이 불가능합니다");
                            }
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        return null;
    }
}
