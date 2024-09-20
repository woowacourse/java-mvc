package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;
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
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        if (!handlerExecutions.containsKey(handlerKey)) {
            handlerExecutions.put(handlerKey, findHandlerMethodByKey(handlerKey));
        }
        return handlerExecutions.get(handlerKey);
    }

    private HandlerExecution findHandlerMethodByKey(HandlerKey handlerKey) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return controllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getDeclaredMethods()))
                .filter(method -> isHandlerMethod(method, handlerKey))
                .findAny()
                .map(HandlerExecution::new)
                .orElseThrow(() -> new NoSuchElementException(handlerKey + "와 일치하는 핸들러 메소드가 없습니다"));
    }

    private boolean isHandlerMethod(Method method, HandlerKey requestKey) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return false;
        }

        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        return Stream.of(annotation.method())
                .map(requestMethod -> new HandlerKey(annotation.value(), requestMethod))
                .anyMatch(key -> key.equals(requestKey));
    }
}
