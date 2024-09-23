package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.keygenerator.HandlerKeyGenerator;
import com.interface21.webmvc.servlet.mvc.tobe.keygenerator.HandlerKeyGeneratorMapping;
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
        Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);

        annotatedControllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getDeclaredMethods()))
                .forEach(this::initMethodMapping);

        log.info("initialized Handler ma");
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.findMethod(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }

    private void initMethodMapping(Method method) {
        HandlerKeyGenerator handlerKeyGenerator = new HandlerKeyGeneratorMapping().match(method);
        if (handlerKeyGenerator == null) {
            return;
        }

        HandlerKey[] handlerKeys = handlerKeyGenerator.makeKeys(method);
        saveHandlerKeys(handlerKeys, method);
    }

    private void saveHandlerKeys(HandlerKey[] handlerKeys, Method method) {
        for (HandlerKey handlerKey : handlerKeys) {
            validateNoDuplicateHandler(handlerKey);
            handlerExecutions.put(handlerKey, new HandlerExecution(method));
        }
    }

    private void validateNoDuplicateHandler(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("이미 등록된 Handler 입니다.");
        }
    }
}
