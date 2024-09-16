package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
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
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class).stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::addHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlers(Method method) {
        HandlerExecution handlerExecution = new HandlerExecution(method);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerKeys handlerKeys = HandlerKeys.from(requestMapping);

        handlerKeys.getKeys()
                        .forEach(handlerKey -> addHandler(handlerKey, handlerExecution));
    }

    private void addHandler(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("이미 존재하는 RequestMethod 입니다.");
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = HandlerKey.of(request);
        return handlerExecutions.get(handlerKey);
    }
}
