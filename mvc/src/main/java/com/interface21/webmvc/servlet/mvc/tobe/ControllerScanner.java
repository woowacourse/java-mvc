package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public Map<HandlerKey, HandlerExecution> scan(String... basePackages) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        for (String packageName : basePackages) {
            scanPackage(packageName, handlerExecutions);
        }

        return handlerExecutions;
    }

    private void scanPackage(String packageName, Map<HandlerKey, HandlerExecution> handlerExecutions) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            try {
                registerController(controller, handlerExecutions);
            } catch (Exception e) {
                log.error("컨트롤러를 등록할 수 없습니다: {}", controller.getName(), e);
            }
        }
    }

    private void registerController(Class<?> controller, Map<HandlerKey, HandlerExecution> handlerExecutions)
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        Object controllerInstance = controller.getDeclaredConstructor().newInstance();
        Method[] methods = controller.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandlerMethod(controllerInstance, method, handlerExecutions);
            }
        }
    }

    private void registerHandlerMethod(Object controllerInstance, Method method, Map<HandlerKey, HandlerExecution> handlerExecutions) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        RequestMethod[] targetMethods;
        if (requestMethods.length == 0) {
            targetMethods = RequestMethod.values();
        } else {
            targetMethods = requestMethods;
        }

        for (RequestMethod requestMethod : targetMethods) {
            registerHandlerExecution(url, requestMethod, controllerInstance, method, handlerExecutions);
        }
    }

    private void registerHandlerExecution(String url, RequestMethod requestMethod, Object controllerInstance, Method method, Map<HandlerKey, HandlerExecution> handlerExecutions) {
        HandlerKey key = new HandlerKey(url, requestMethod);
        HandlerExecution execution = new HandlerExecution(controllerInstance, method);
        handlerExecutions.put(key, execution);
    }
}