package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.execution.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.support.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.support.RequestPath;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Map<Class<?>, Object> controllerRegistry = scanControllers();
        registerHandlers(controllerRegistry);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        RequestPath path = new RequestPath(request.getRequestURI());
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(path.getValue(), RequestMethod.from(method)));
    }

    private Map<Class<?>, Object> scanControllers() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        return controllerScanner.getControllers();
    }

    // 전체 컨트롤러를 순회하며 등록
    private void registerHandlers(Map<Class<?>, Object> controllerRegistry) {
        Set<Class<?>> controllers = controllerRegistry.keySet();
        controllers.forEach(controller -> registerControllerMethods(controller, controllerRegistry.get(controller)));
    }

    // 단일 컨트롤러의 모든 RequestMapping 메서드를 등록
    private void registerControllerMethods(Class<?> controllerClass, Object controllerInstance) {
        Method[] methods = controllerClass.getDeclaredMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandler(controllerInstance, method));
    }

    // 단일 컨트롤러 메서드 등록
    private void registerHandler(Object controllerInstance, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        RequestPath path = new RequestPath(mapping.value());
        RequestMethod[] requestMethods = resolveRequestMethods(mapping.method());

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(path.getValue(), requestMethod);
            validateDuplicatedKey(key);
            HandlerExecution execution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(key, execution);

            log.info("Registered Handler - Controller: {}, Method: {}, Path: {}, HTTP Method: {}",
                    controllerInstance.getClass().getSimpleName(),
                    method.getName(),
                    path.getValue(),
                    requestMethod);
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMethod[] requestMethods) {
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    private void validateDuplicatedKey(HandlerKey key) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalStateException("Handler Key already exists :" + key);
        }
    }
}
