package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.el.MethodNotFoundException;
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
    private static final String NOT_FOUND_HANDLER_NAME = "handleNotFound";

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        initialize();
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        String uri = request.getRequestURI();

        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }

        return createNotFoundHandlerExecution();
    }

    private HandlerExecution createNotFoundHandlerExecution() {
        try {
            DefaultController defaultController = DefaultController.getInstance();

            Method notFoundMethod = Arrays.stream(defaultController.getClass().getDeclaredMethods())
                    .filter(method -> method.getName().equals(NOT_FOUND_HANDLER_NAME))
                    .findAny()
                    .orElseThrow(MethodNotFoundException::new);

            return new HandlerExecution(notFoundMethod, defaultController);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Class<?> clazz : controllers.keySet()) {
            List<Method> requestMappingMethods = findRequestMappingMethods(clazz);
            mapHandlers(requestMappingMethods, controllers.get(clazz));
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Method> findRequestMappingMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void mapHandlers(List<Method> mapperMethods, Object controller) {
        for (Method mapperMethod : mapperMethods) {
            HandlerExecution handlerExecution = new HandlerExecution(mapperMethod, controller);
            makeHandlerKeys(mapperMethod)
                    .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }
    }

    private List<HandlerKey> makeHandlerKeys(Method mapperMethod) {
        RequestMapping annotation = mapperMethod.getAnnotation(RequestMapping.class);
        String uri = annotation.value();

        return Arrays.stream(annotation.method())
                .map(method -> new HandlerKey(uri, method))
                .toList();
    }
}
