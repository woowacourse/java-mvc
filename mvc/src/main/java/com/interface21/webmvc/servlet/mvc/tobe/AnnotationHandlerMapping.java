package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.utils.InstanceManager;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final int EMPTY_REQUEST_METHOD_SIZE = 0;

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = new InstanceManager(basePackage).getControllers();
        controllers.forEach(this::registerController);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(Class<?> controllerClass, Object controllerInstance) {
        List<Method> methods = registerControllerMethods(controllerClass);
        methods.forEach(method -> registerHandlerMapping(controllerInstance, method));
    }

    private List<Method> registerControllerMethods(Class<?> controller) {
        Method[] methods = controller.getDeclaredMethods();

        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerHandlerMapping(Object instance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = determineRequestMethods(requestMapping);

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(instance, method));
        }
    }

    private RequestMethod[] determineRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == EMPTY_REQUEST_METHOD_SIZE) {
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));

        return Optional.ofNullable(handlerExecutions.get(handlerKey));
    }
}
