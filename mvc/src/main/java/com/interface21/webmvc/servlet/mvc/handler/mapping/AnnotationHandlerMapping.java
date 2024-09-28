package com.interface21.webmvc.servlet.mvc.handler.mapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.handler.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        initHandlerExecutions();
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutions() {
        Reflections reflections = new Reflections(basePackages);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::registerControllerMethods);
    }

    private void registerControllerMethods(Class<?> controllerClass) {
        Object controller = createControllerInstance(controllerClass);
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandlerExecution(controller, method));
    }

    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return ReflectionUtils.accessibleConstructor(controllerClass).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("컨트롤러 객체를 생성하는데 실패했습니다.", e);
        }
    }

    private void registerHandlerExecution(Object controllerInstance, Method method) {
        HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();

        Arrays.stream(requestMapping.method())
                .forEach(requestMethod ->
                        registerHandlerExecution(new HandlerKey(url, requestMethod), handlerExecution));
    }

    private void registerHandlerExecution(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException(String.format("%s %s에 대한 핸들러가 중복됩니다.",
                    handlerKey.getRequestMethod(), handlerKey.getUrl()));
        }

        handlerExecutions.put(handlerKey, handlerExecution);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
