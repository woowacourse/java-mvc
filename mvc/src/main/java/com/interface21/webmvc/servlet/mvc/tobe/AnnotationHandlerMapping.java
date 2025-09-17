package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for(Object basePackage : basePackages) {
            ConfigurationBuilder config = new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(basePackage.toString()))
                    .setScanners(Scanners.TypesAnnotated, Scanners.MethodsAnnotated);

            Reflections reflections = new Reflections(config);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            try {
                for (Class<?> controllerClass : controllerClasses) {
                    Object handler = controllerClass.getDeclaredConstructor().newInstance();
                    for (Method method : controllerClass.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            registerHandler(handler, method);
                        }
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                log.info(e.getMessage());
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private void registerHandler(Object handler, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] httpMethods = requestMapping.method();

        HandlerExecution handlerExecution = new HandlerExecution(handler, method);

        String path = determinePath(requestMapping);

        if (httpMethods.length == 0) {
            for (RequestMethod requestMethod : RequestMethod.values()) {
                HandlerKey handlerKey = new HandlerKey(path, requestMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
            return;
        }
        for (RequestMethod requestMethod : httpMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private String determinePath(RequestMapping requestMapping) {
        if(requestMapping.value().isEmpty()) {
            return "";
        }
        return requestMapping.value();
    }
}
