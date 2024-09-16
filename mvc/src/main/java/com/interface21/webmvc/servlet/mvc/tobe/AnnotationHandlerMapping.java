package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        List<Object> controllerInstances = getControllerInstances(basePackage);

        for (Object controller : controllerInstances) {
            processController(controller);
        }
    }

    private List<Object> getControllerInstances(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return controllers.stream()
                .map(this::makeControllerInstance)
                .toList();
    }

    private Object makeControllerInstance(Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void processController(Object controller) {
        Method[] methods = controller.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                processMethod(controller, method);
            }
        }
    }

    private void processMethod(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();  // 가정: value는 url 패턴을 반환
        RequestMethod[] requestMethods = requestMapping.method();  // method는 RequestMethod 배열을 반환

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            addHandlerExecution(handlerKey, handlerExecution);
        }
    }

    private void addHandlerExecution(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        handlerExecutions.put(handlerKey, handlerExecution);
        log.info("Mapped {} to {}", handlerKey, handlerExecution);
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String rawMethod = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(rawMethod.toUpperCase());

        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
