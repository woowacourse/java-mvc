package com.interface21.webmvc.servlet.mvc.handler.mapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.handler.HandlerKey;
import com.interface21.webmvc.servlet.mvc.handler.HandlingException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final Set<Object> controllers;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.controllers = new HashSet<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.addAll(createControllerInstances(controllerTypes));

        controllerTypes.stream()
                .flatMap(type -> Arrays.stream(type.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::addHandler);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Object> createControllerInstances(Set<Class<?>> controllerTypes) {
        return controllerTypes.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new HandlingException("해당 컨트롤러의 기본 생성자로 인스턴스를 생성할 수 없습니다.");
        }
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
        Object controller = findDeclaringController(handler);
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new HandlingException("같은 요청을 처리할 메서드가 두 개 이상 존재합니다.");
        }
        handlerExecutions.put(handlerKey, new HandlerExecution(controller, handler));
    }

    private Object findDeclaringController(Method handler) {
        return controllers.stream()
                .filter(controller -> Objects.equals(controller.getClass(), handler.getDeclaringClass()))
                .findAny()
                .orElseThrow(() -> new HandlingException("해당 메서드를 실행할 컨트롤러가 존재하지 않습니다."));
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
