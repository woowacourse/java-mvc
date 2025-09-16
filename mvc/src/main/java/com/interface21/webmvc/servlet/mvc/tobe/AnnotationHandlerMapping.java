package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        for (final Object packageName : basePackage) {
            scanPackageForControllers(packageName.toString());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void scanPackageForControllers(final String packageName) {
        final List<Class<?>> classes = getClassesInPackage(packageName);

        for (final Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                registerHandlerMethods(clazz);
            }
        }
    }

    private List<Class<?>> getClassesInPackage(final String packageName) {
        final List<Class<?>> classes = new ArrayList<>();
        final String path = packageName.replace('.', '/');

        try {
            final URL resource = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(path);

            if (resource == null) {
                log.warn("Package not found: {}", packageName);
                return classes;
            }

            final File directory = new File(resource.getFile());
            if (directory.exists()) {
                scanDirectory(directory, packageName, classes);
            }
        } catch (Exception e) {
            log.error("Error scanning package: {}", packageName, e);
        }

        return classes;
    }

    private void scanDirectory(final File directory, final String packageName, final List<Class<?>> classes) {
        final File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (final File file : files) {
            final String fileName = file.getName();

            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + fileName, classes);
                continue;
            }

            if (fileName.endsWith(".class")) {
                addPackageClass(packageName, classes, fileName);
            }
        }
    }

    private void addPackageClass(final String packageName, final List<Class<?>> classes, final String fileName) {
        final String className = packageName + "." + fileName.substring(0, fileName.length() - 6);
        try {
            final Class<?> clazz = Class.forName(className);
            classes.add(clazz);
        } catch (ClassNotFoundException e) {
            log.warn("Could not load class: {}", className);
        }
    }

    private void registerHandlerMethods(final Class<?> controllerClass) {
        try {
            final Object controller = controllerClass.getDeclaredConstructor().newInstance();

            for (final Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    registerHandlerMethod(method, controller);
                }
            }
        } catch (Exception e) {
            log.error("Error registering handler methods for class: {}", controllerClass.getName(), e);
        }
    }

    private void registerHandlerMethod(final Method method, final Object controller) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        registerMapping(controller, method, url, requestMethods);
    }

    private void registerMapping(
            final Object controller,
            final Method method,
            final String url,
            final RequestMethod[] requestMethods
    ) {
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String httpMethod = request.getMethod();

        final RequestMethod requestMethod = RequestMethod.valueOf(httpMethod);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
