package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javassist.NotFoundException;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }

    public Object getHandler(final HttpServletRequest request) throws NotFoundException {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder
                .setUrls(ClasspathHelper.forPackage("samples"))
                .setScanners(new MethodAnnotationsScanner());

        Reflections reflections = new Reflections(configurationBuilder);
        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        Method targetMethod = methods.stream()
                .filter(method -> method.getAnnotation(RequestMapping.class).value().equals(request.getRequestURI()))
                .filter(method -> method.getAnnotation(RequestMapping.class).method()[0].name()
                        .equals(request.getMethod()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No resource found"));

        Object controller;
        try {
            controller = targetMethod.getDeclaringClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Internal error: Failed to create controller instance", e);
        }
        return new HandlerExecution(controller, targetMethod);
    }
}
