package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        addHandlerExecutions(controllers);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers) {
        for (Entry<Class<?>, Object> controller : controllers.entrySet()) {
            for (Method method : scanControllerMethods(controller.getKey())) {
                addHandlerExecutionsByController(controller.getValue(), method);
            }
        }
    }

    private static List<Method> scanControllerMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addHandlerExecutionsByController(Object instanceController, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        String url = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        List<HandlerKey> handlerKeys = mapHandlerKeys(url, requestMethods);

        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, new HandlerExecution(instanceController, method));
        }
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();

        for (RequestMethod requestMethod : requestMethods) {
            handlerKeys.add(new HandlerKey(url, requestMethod));
        }
        return handlerKeys;
    }

    public RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    @Override
    public boolean handlerExist(HttpServletRequest httpServletRequest) {
        String requestURI = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        return handlerExecutions.containsKey(handlerKey);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), getRequestMethod(request));
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);

        if (handlerExecution == null) {
            throw new IllegalArgumentException("핸들러가 존재하지 않습니다.");
        }

        return handlerExecution;
    }

    private RequestMethod getRequestMethod(HttpServletRequest request) {
        try {
            return RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("잘못된 HTTP 메서드입니다.");
        }
    }
}
