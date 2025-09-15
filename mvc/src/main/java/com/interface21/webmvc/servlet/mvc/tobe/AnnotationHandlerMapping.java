package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            mapController(controller);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void mapController(final Class<?> controller) {
        List<Method> annotatedMethods = findMappedMethods(controller);

        for (Method method : annotatedMethods) {
            try {
                addHandlerExecution(controller, method);
            } catch (Exception e) {
                log.error("Failed to add handler execution.");
                throw new RuntimeException(e);
            }
        }
    }

    private List<Method> findMappedMethods(final Class<?> controller) {
        Method[] declaredMethods = controller.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addHandlerExecution(final Class<?> controller, final Method method) throws Exception {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String url = annotation.value();
        RequestMethod[] requestMethods = annotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            Constructor<?> controllerConstructor = controller.getDeclaredConstructor();
            HandlerExecution handlerExecution = new HandlerExecution(controllerConstructor.newInstance(), method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}
