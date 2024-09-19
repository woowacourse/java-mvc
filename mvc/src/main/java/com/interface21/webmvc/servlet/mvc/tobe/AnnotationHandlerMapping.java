package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
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
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .flatMap(type -> Arrays.stream(type.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::addHandler);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandler(final Method handler) {
        RequestMapping annotation = handler.getAnnotation(RequestMapping.class);
        String uri = annotation.value();
        List<RequestMethod> methods = Arrays.asList(annotation.method());
        if (methods.isEmpty()) {
            addHandlerForAllRequestMethods(handler, uri);
        }
        methods.forEach(method -> addHandlerForRequestMethod(handler, method, uri));
    }

    private void addHandlerForAllRequestMethods(final Method handler, final String uri) {
        Arrays.stream(RequestMethod.values())
                .forEach(method -> addHandlerForRequestMethod(handler, method, uri));
    }

    private void addHandlerForRequestMethod(final Method handler, final RequestMethod method, final String uri) {
        HandlerKey handlerKey = new HandlerKey(uri, method);
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new HandlingException("같은 요청을 처리할 메서드가 두 개 이상 존재합니다.");
        }
        handlerExecutions.put(handlerKey, new HandlerExecution(handler));
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new HandlingException("요청을 처리할 핸들러가 존재하지 않습니다.");
        }
        return handlerExecution;
    }
}
