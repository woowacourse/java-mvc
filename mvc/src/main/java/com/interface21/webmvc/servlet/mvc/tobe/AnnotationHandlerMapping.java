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
import java.util.NoSuchElementException;
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
        List<Method> handlers = findAnnotatedMethods();
        handlers.forEach(this::register);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Method> findAnnotatedMethods() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .flatMap(controller -> Arrays.stream(controller.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void register(Method handler) {
        RequestMapping annotation = handler.getDeclaredAnnotation(RequestMapping.class);
        Object instance = createHandlerInstance(handler);
        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handler, instance);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object createHandlerInstance(Method handler) {
        try {
            return handler.getDeclaringClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("인스턴스를 생성할 수 없습니다.");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKey(request);
        return findHandler(handlerKey);
    }

    private HandlerKey createHandlerKey(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        return new HandlerKey(uri, RequestMethod.valueOf(method));
    }

    private HandlerExecution findHandler(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        throw new NoSuchElementException("요청을 처리할 핸들러를 찾을 수 없습니다.");
    }
}
