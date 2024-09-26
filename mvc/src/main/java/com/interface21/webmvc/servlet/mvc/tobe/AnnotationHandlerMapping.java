package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.exception.HandlerInitializationException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(ControllerScanner controllerScanner) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = controllerScanner;
    }


    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, method);

        return handlerExecutions.get(handlerKey);
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::addHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlers(final Class<?> controllerType, final Object instance) {
        try {
            Arrays.stream(controllerType.getMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandler(method, instance));
        } catch (Exception e) {
            throw new HandlerInitializationException("핸들러 초기화에서 예외가 발생했습니다.", e);
        }
    }

    private void addHandler(final Method method, final Object controllerInstance) {
        HandlerExecution handlerExecution = createHandlerExecution(method, controllerInstance);
        createHandlerKeys(method).forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    private List<HandlerKey> createHandlerKeys(final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .toList();
    }

    private HandlerExecution createHandlerExecution(final Method method, final Object controller) {
        return new HandlerExecution(method, controller);
    }
}
