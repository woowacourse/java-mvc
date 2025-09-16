package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            try {
                Object controllerInstance = createControllerInstance(controller);
                registerRequestMappings(controllerInstance, controller);
            } catch (Exception e) {
                log.error("컨트롤러 시작 에러: {}", controller.getName(), e);
            }
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }

    private Object createControllerInstance(Class<?> controller) throws Exception {
        Constructor<?> constructor = controller.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private void registerRequestMappings(Object controllerInstance, Class<?> controllerClass) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            for (RequestMethod httpMethod : mapping.method()) {
                HandlerKey key = new HandlerKey(mapping.value(), httpMethod);
                handlerExecutions.put(key, new HandlerExecution(controllerInstance, method));
            }
        }
    }
}
