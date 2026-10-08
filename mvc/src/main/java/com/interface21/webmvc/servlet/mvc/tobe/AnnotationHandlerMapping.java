package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        for (Class<?> controllerClass : controllers) {
            Object controller = createInstance(controllerClass);
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    addHandlerExecution(controller, method);
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + clazz.getName(), e);
        }
    }

    private void addHandlerExecution(Object controller, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        String url = mapping.value();
        RequestMethod[] requestMethods = mapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
