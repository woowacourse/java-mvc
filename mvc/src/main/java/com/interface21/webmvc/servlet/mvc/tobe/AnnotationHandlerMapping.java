package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getController();
        registerController(controllers);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private void registerController(Map<Class<?>, Object> controllers) {
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> controllerClass = entry.getKey();
            Object controller = entry.getValue();

            for (Method method : ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class))) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                validateHandlerMethod(controller, method);
                registerHandlerMethods(controller, method, mapping);
            }
        }
    }

    private static void validateHandlerMethod(Object controller, Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalStateException("@RequestMapping 메서드는 public이어야 합니다: " + method);
        }

        Object target = controller;
        if (Modifier.isStatic(method.getModifiers())) {
            target = null;
        }
        if (!method.canAccess(target)) {
            throw new IllegalStateException("요청 핸들러를 호출할 수 없습니다: " + method);
        }
    }

    private void registerHandlerMethods(Object controller, Method method, RequestMapping mapping) {
        RequestMethod[] methods = mapping.method();
        if (methods.length == 0) {
            methods = RequestMethod.values();
        }

        String uri = mapping.value();
        for (RequestMethod requestMethod : methods) {
            HandlerExecution existing = handlerExecutions.putIfAbsent(
                    new HandlerKey(uri, requestMethod),
                    new HandlerExecution(controller, method)
            );
            if (existing != null) {
                throw new IllegalArgumentException("중복된 키 값 입니다.");
            }
        }
    }
}
