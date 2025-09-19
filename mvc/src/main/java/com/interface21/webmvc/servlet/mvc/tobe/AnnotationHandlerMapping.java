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
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("AnnotationHandlerMapping 초기화");

        for (Object packageName : basePackage) {
            scanPackageForControllers(packageName.toString());
        }
    }

    private void scanPackageForControllers(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            URL resource = classLoader.getResource(path);

            if (resource == null) {
                log.warn("Package를 찾을 수 없음: {}", packageName);
                return;
            }

            File directory = new File(resource.getFile());
            if (directory.exists()) {
                scanDirectory(directory, packageName);
            }
        } catch (Exception e) {
            log.error("scanning package 오류: {}", packageName, e);
        }
    }

    private void scanDirectory(File directory, String packageName) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        registerController(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    log.warn("Class를 찾을 수 없음: {}", className);
                }
            } else if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            }
        }
    }

    private void registerController(Class<?> controllerClass) {
        try {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            Method[] methods = controllerClass.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String url = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();

                    if (requestMethods.length == 0) {
                        for (RequestMethod requestMethod : RequestMethod.values()) {
                            HandlerKey key = new HandlerKey(url, requestMethod);
                            HandlerExecution execution = new HandlerExecution(controllerInstance, method);
                            handlerExecutions.put(key, execution);
                        }
                    } else {
                        for (RequestMethod requestMethod : requestMethods) {
                            HandlerKey key = new HandlerKey(url, requestMethod);
                            HandlerExecution execution = new HandlerExecution(controllerInstance, method);
                            handlerExecutions.put(key, execution);
                        }
                    }

                    log.info("Handler 등록: {} -> {}.{}", url, controllerClass.getSimpleName(), method.getName());
                }
            }
        } catch (Exception e) {
            log.error("Controller 등록 오류: {}", controllerClass.getName(), e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String methodName = request.getMethod();

        try {
            RequestMethod requestMethod = RequestMethod.valueOf(methodName);
            HandlerKey key = new HandlerKey(requestURI, requestMethod);
            return handlerExecutions.get(key);
        } catch (IllegalArgumentException e) {
            log.warn("지원하지 않는 HTTP method: {}", methodName);
            return null;
        }
    }
}
