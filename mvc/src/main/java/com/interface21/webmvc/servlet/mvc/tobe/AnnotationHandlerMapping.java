package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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

        controllers.forEach(this::reflect);
    }

    private void reflect(Class<?> controller) {
        try {
            Object instance = controller.getConstructor().newInstance();
            Method[] methods = controller.getDeclaredMethods();

            Arrays.stream(methods).forEach(method -> addHandlerExecution(instance, method));

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException("controller 리플렉션 중 실패");
        }
    }

    private void addHandlerExecution(Object handlerInstance, Method handlerMethod) {

        String uri = (String) executeMethod("value", handlerMethod);
        RequestMethod[] requestMethods = (RequestMethod[]) executeMethod("method", handlerMethod);

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        HandlerExecution handlerExecution = new HandlerExecution(handlerInstance, handlerMethod);

        Arrays.stream(requestMethods).forEach(requestMethod -> {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        });
    }

    private Object executeMethod(String methodName, Method method) {
        try {
            Annotation annotation = method.getAnnotation(RequestMapping.class);
            return annotation.getClass()
                    .getDeclaredMethod(methodName)
                    .invoke(annotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("controller method 리플렉션 중 실패");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        throw new UnsupportedOperationException("지원하지 않는 요청입니다.");
    }
}
