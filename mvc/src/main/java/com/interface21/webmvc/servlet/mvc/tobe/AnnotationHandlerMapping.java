package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.reflections.Reflections;
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
        final Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
            .forEach(this::registerController);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Class<?> controllerClass) {
        final Object controller = getController(controllerClass);

        Arrays.stream(controllerClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .forEach(method -> addHandlerExecutions(controller, method));
    }

    private void addHandlerExecutions(final Object controller, final Method method) {
        final HandlerExecution execution = HandlerExecution.from(controller, method);
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String requestUrl = requestMapping.value();
        final List<RequestMethod> availableRequestMethods = getAvailableRequestMethods(requestMapping);

        availableRequestMethods.forEach(requestMethod -> {
            final HandlerKey key = new HandlerKey(requestUrl, requestMethod);

            handlerExecutions.put(key, execution);
        });
    }

    private List<RequestMethod> getAvailableRequestMethods(final RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return List.of(RequestMethod.values());
        }
        return List.of(requestMapping.method());
    }

    private Object getController(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor()
                .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey =
            new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
