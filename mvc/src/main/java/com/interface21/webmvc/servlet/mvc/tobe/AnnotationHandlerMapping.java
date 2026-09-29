package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllers) {
            Object controller = createController(controllerClass);

            Arrays.stream(controllerClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> {
                        registerHandler(controller, method);
                    });
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandler(Object controller, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] declaredMethods = mapping.method();
        RequestMethod[] requestMethods = declaredMethods.length == 0 ? RequestMethod.values() : declaredMethods;

        Arrays.stream(requestMethods)
                .forEach(requestMethod -> {
                    HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
                    HandlerExecution handler = new HandlerExecution(controller, method);

                    if (handlerExecutions.putIfAbsent(key, handler) != null) {
                        throw new IllegalStateException(
                                "중복된 핸들러 매핑입니다: "
                                        + requestMethod + " " + mapping.value()
                        );
                    }
                });
    }

    @Nonnull
    private static Object createController(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(), e);
        }
    }


    public Object getHandler(final HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String path = request.getRequestURI().substring(contextPath == null ? 0 : contextPath.length());
        HandlerKey handlerKey = new HandlerKey(path, RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
