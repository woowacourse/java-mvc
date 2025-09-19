package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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
        for (Object packageName : basePackage) {
            getController(packageName.toString());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);

        HandlerKey key = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(key);
    }

    private void getController(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            try {
                registerHandler(controller);
            } catch (Exception e) {
                log.error("컨트롤러를 등록할 수 없습니다: {}", controller.getName(), e);
            }
        }
    }

    private void registerHandler(Class<?> controller)
            throws InstantiationException,IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        Object controllerInstance = controller.getDeclaredConstructor().newInstance();
        Method[] methods = controller.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandlerMethod(controllerInstance, method);
            }
        }
    }

    private void registerHandlerMethod(Object controllerInstance, Method method) {
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
            registerHandlerExecution(url, requestMethod, controllerInstance, method);
        }
    }

    private void registerHandlerExecution(String url, RequestMethod requestMethod, Object controllerInstance, Method method) {
        HandlerKey key = new HandlerKey(url, requestMethod);
        HandlerExecution execution = new HandlerExecution(controllerInstance, method);
        handlerExecutions.put(key, execution);
    }
}
