package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
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
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::registerController);
    }

    private void registerController(Class<?> clazz, Object instance) {
        final Object controller;
        try {
            controller = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("컨트롤러를 생성할 수 없습니다.", e);
        }
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    method.setAccessible(true);
                    registerHandlerMethod(controller, method);
                });
    }

    private void registerHandlerMethod(Object controller, Method handlerMethod) {
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        RequestMethod[] httpMethods = requestMapping.method();
        if (httpMethods.length == 0) {
            httpMethods = RequestMethod.values();
        }
        for (RequestMethod httpMethod : httpMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), httpMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, handlerMethod);
            System.out.println(handlerKey);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("중복된 핸들러 매핑이 존재합니다: " + handlerKey);
            }
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.valueOf(method)));
    }
}
