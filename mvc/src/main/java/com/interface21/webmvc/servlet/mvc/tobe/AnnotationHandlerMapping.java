package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

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
        
        for (final Object pkg : basePackage) {
            final String packageName = pkg.toString();
            log.info("Scanning package: {}", packageName);
            scanPackage(packageName);
        }
        
        log.info("Total handlers registered: {}", handlerExecutions.size());
        handlerExecutions.keySet().forEach(key -> 
            log.info("Registered handler: {}", key));
    }

    private void scanPackage(final String packageName) {
        try {
            final String path = packageName.replace('.', '/');
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final Enumeration<URL> resources = classLoader.getResources(path);
            
            while (resources.hasMoreElements()) {
                final URL resource = resources.nextElement();
                final File file = new File(resource.getFile());
                
                if (file.exists() && file.isDirectory()) {
                    scanDirectory(file, packageName);
                }
            }
        } catch (final Exception e) {
            log.error("Package scan failed: {}", packageName, e);
        }
    }

    private void scanDirectory(final File directory, final String packageName) {
        final File[] files = directory.listFiles();
        if (files == null) return;
        
        for (final File file : files) {
            processDirectoryFile(file, packageName);
        }
    }

    private void processDirectoryFile(final File file, final String packageName) {
        if (file.isDirectory()) {
            scanDirectory(file, packageName + "." + file.getName());
            return;
        }
        
        if (file.getName().endsWith(".class")) {
            final String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
            processClass(className);
        }
    }

    private void processClass(final String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            
            if (isController(clazz)) {
                registerController(clazz);
            }
        } catch (final ClassNotFoundException e) {
            log.debug("Class not found: {}", className);
        } catch (final Exception e) {
            log.warn("Failed to process class: {}", className, e);
        }
    }

    private boolean isController(final Class<?> clazz) {
        return clazz.isAnnotationPresent(Controller.class);
    }

    private void registerController(final Class<?> controllerClass) {
        try {
            final Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            
            for (final Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    registerHandlerMethod(controllerInstance, method);
                }
            }
        } catch (final Exception e) {
            log.error("Failed to register controller: {}", controllerClass.getName(), e);
        }
    }

    private void registerHandlerMethod(final Object controllerInstance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        final RequestMethod[] methods = requestMapping.method();
        
        final RequestMethod[] targetMethods = determineTargetMethods(methods);
        registerHandlerForMethods(controllerInstance, method, url, targetMethods);
        
        log.debug("Registered handler: {} -> {}.{}", url, controllerInstance.getClass().getSimpleName(), method.getName());
    }

    private RequestMethod[] determineTargetMethods(final RequestMethod[] methods) {
        return methods.length == 0 ? RequestMethod.values() : methods;
    }

    private void registerHandlerForMethods(final Object controllerInstance, final Method method, final String url, final RequestMethod[] targetMethods) {
        for (final RequestMethod requestMethod : targetMethods) {
            final HandlerKey key = new HandlerKey(url, requestMethod);
            final HandlerExecution execution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(key, execution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey key = createHandlerKey(request);
        if (key == null) {
            return null;
        }
        
        return handlerExecutions.get(key);
    }

    private HandlerKey createHandlerKey(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final String httpMethod = request.getMethod();
        
        try {
            final RequestMethod requestMethod = RequestMethod.valueOf(httpMethod);
            return new HandlerKey(uri, requestMethod);
        } catch (final IllegalArgumentException e) {
            log.warn("Unsupported HTTP method: {}", httpMethod);
            return null;
        }
    }
}
